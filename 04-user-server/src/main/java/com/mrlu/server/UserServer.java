package com.mrlu.server;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mrlu.server.bean.UserDO;
import com.mrlu.server.dao.UserDao;
import com.mrlu.server.util.Log;
import com.mrlu.server.util.MybatisUtils;
import fai.comm.util.FaiList;
import fai.comm.util.Param;
import org.apache.ibatis.session.SqlSession;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * @author 简单de快乐（陆朝基）
 * @date 2021-06-07 12:35
 */
public class UserServer {

    private static ObjectInputStream in;
    private static ObjectOutputStream out;
    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(5, Runtime.getRuntime().availableProcessors() + 1,
            10, TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(10),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * 缓存更新的用户
     */
    private static Param cache = new Param(true);

    private SqlSession sqlSession = MybatisUtils.getSqlSession();
    private UserDao userDao = sqlSession.getMapper(UserDao.class);
    protected static Log logger = Log.getInstance();


    public static void main(String[] args) {
            try {
                ServerSocket serverSocket = new ServerSocket(8899);
                logger.standardRecord(Log.LOG_STANDARD,"启动服务端...");
                Socket socket;
                while (true) {
                    socket = serverSocket.accept();
                    invoke(socket);
                }
            } catch (IOException e) {
                logger.errorRecord(Log.LOG_ERROR, e,"连接失败错误，客户端或者服务端Socket未打开");

            }
    }

    public static void invoke(Socket client) {
        THREAD_POOL.execute(()-> {
            try {
                in = new ObjectInputStream(client.getInputStream());
                Map<String, Object> map = (Map<String, Object>) in.readObject();
                if (map != null) {
                    UserServer userServer = new UserServer();
                    Method method = userServer.getClass().getDeclaredMethod((String) map.get("action"),
                            Socket.class, Map.class);
                    //避免多个if-elseif-else分支进行，造成代码的冗余。
                    method.invoke(userServer, client, map);
                }
            } catch (IOException e) {
                logger.errorRecord(Log.LOG_ERROR, e,"类型找不到错误，客户端传输的数据类型在服务端不存在");
            } catch (ReflectiveOperationException e) {
                logger.errorRecord(Log.LOG_ERROR, e,"反射错误，服务端没有对应的方法");

            }
        });
    }

    public void listsUser(Socket client, Map<String, Object> map) {
        try {
            out = new ObjectOutputStream(client.getOutputStream());
            //引入PageHelper分页插件。传入页码以及每页显示的数量
            int pageNum = (int) map.get("pageNum");
            int pageSize = (int) map.get("pageSize");
            PageHelper.startPage(pageNum, pageSize);
            List<UserDO> result = userDao.listsUserDo();
            //pageInfo包含了查询的结果。以及详细的分页信息。这里是连续显示5页
            PageInfo<UserDO> pageInfo = new PageInfo<>(result, 5);

            //把结果保存到map中进行传输
            map.put("data", pageInfo);
            out.writeObject(map);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(client, in, out);

        }
    }

    public void getUser(Socket client, Map<String, Object> map) {
        try {
            out = new ObjectOutputStream(client.getOutputStream());
            String userId = "userId";
            UserDO userDO;
            String name = (String) map.get("name");
            boolean hasCached = cache.containsKey(name);
            if (hasCached) {
                userDO = (UserDO) cache.getObject(name);
            } else { //如果没有缓存
                if (map.containsKey(userId)) {
                    Integer id = (int) map.get(userId);
                    userDO = userDao.getUserDoById(id);
                } else {
                    userDO = userDao.getUserDoByName(name);
                }
                cache.setObject(userDO.getName(), userDO);
            }
            map.put("data", userDO);
            out.writeObject(map);
            out.flush();
        } catch (IOException e) {
            logger.errorRecord(Log.LOG_ERROR, e,"连接失败错误，客户端或者服务端Socket未打开");
        } finally {
            close(client, in , out);

        }
    }

    public void updateUser(Socket client, Map<String, Object> map) {
        try {
            out = new ObjectOutputStream(client.getOutputStream());
            UserDO userDO = (UserDO) map.get("userDO");

            //判断远程传输要更新的用户是否为空，以及该用户是否存在。
            boolean exists = Objects.nonNull(userDO) && (cache.containsKey(userDO.getName()));
            if (!exists) {
                Integer failureCode = 100;
                map.put("responseCode", failureCode);
                out.writeObject(map);
                out.flush();
                return;
            }

            //如果内容没有改变的话，就不更新
            boolean same = userDO.equals(cache.getObject(userDO.getName()));
            if (same) {
                out.writeObject(map);
                out.flush();
                return;
            }

            //设置修改的时间
            userDO.setGmtModified(LocalDateTime.now());
            if (userDO.getId() != null) {
                userDao.updateUserById(userDO);
                userDO = userDao.getUserDoById(userDO.getId());
            } else {
                userDao.updateUserByName(userDO);
                userDO = userDao.getUserDoByName(userDO.getName());
            }
            sqlSession.commit();
            logger.debugRecord(Log.LOG_DUBUG, "更新后的用户："+userDO);

            //更新缓存
            cache.setObject(userDO.getName(), userDO);
            out.writeObject(map);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(client, in , out);

        }
    }

    private void close(Socket client, InputStream input, OutputStream out) {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
