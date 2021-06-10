package com.mrlu.client.web;


import com.github.pagehelper.PageInfo;
import com.mrlu.client.util.Log;
import com.mrlu.server.bean.UserDO;
import fai.comm.util.FaiList;
import fai.comm.util.Param;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.print.Book;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author 简单de快乐（陆朝基）
 * @date 2021-06-07 15:05
 */
public class UserServlet extends BaseServlet {

     public void listsUser(HttpServletRequest request, HttpServletResponse response) {
          try {
               String action = request.getParameter("action");
               int pageNum = Integer.parseInt(request.getParameter("pageNum"));
               //由于Param类没有实现序列化接口且Param内置的Map没有提供get方法，就手动创建Map来保存请求参数
               Map<String, Object> params = new HashMap<>(16);
               params.put("action", action);
               params.put("pageNum", pageNum);
               params.put("pageSize", PAGE_SIZE);

               params = transfer(params);

               PageInfo<UserDO> pageInfo = (PageInfo<UserDO>) params.get("data");
               if (Objects.nonNull(pageInfo)) {
                    request.setAttribute("page",pageInfo);
                    request.getRequestDispatcher("/pages/index.jsp").forward(request, response);
               }
          } catch (IOException e) {
               logger.errorRecord(Log.LOG_ERROR, e,"连接失败错误，服务端ServerSocket未打开");
          } catch (ClassNotFoundException e) {
               logger.errorRecord(Log.LOG_ERROR, e,"类型找不到错误，服务端回传的类型在客户端不存在");
          } catch (ServletException e) {
               logger.errorRecord(Log.LOG_ERROR, e,"重定向到首页错误，重定向的地址有误");
          } finally {
               close(socket, in ,out);

          }
     }

     /**
      * 更新前先获取该用户修改前的信息，同时也起到检查该用户是否存在
      * @param request
      * @param response
      */
     public void getUser(HttpServletRequest request, HttpServletResponse response) {
          String action = request.getParameter("action");
          int pageNum = Integer.parseInt(request.getParameter("pageNum"));
          Map<String, Object> params = new HashMap<>(16);
          params.put("action", action);
          try {
               int id = Integer.parseInt(request.getParameter("id"));
               params.put("userId", id);
          } catch (NumberFormatException e) {
               String name = request.getParameter("name");
               params.put("name", name);
          }

          try {
               params = transfer(params);
               UserDO userDO = (UserDO) params.get("data");
               logger.debugRecord(Log.LOG_DUBUG, "修改用户信息前查询到的用户："+userDO);
               HttpSession session = request.getSession();
               session.setAttribute("user", userDO);
               session.setAttribute("pageNum", pageNum);

               Param responseResult = new Param();
               responseResult.setObject("responseCode",200);
               response.getWriter().write(responseResult.toJson());
          } catch (IOException e) {
               logger.errorRecord(Log.LOG_ERROR, e,"连接失败错误，服务端ServerSocket未打开");
          } catch (ClassNotFoundException e) {
               logger.errorRecord(Log.LOG_ERROR, e,"类型找不到错误，服务端回传的数据类型在客户端不存在");
          } finally {
               close(socket, in ,out);

          }
     }

     public void updateUser(HttpServletRequest request, HttpServletResponse response) {

          String action = request.getParameter("action");
          int pageNum = Integer.parseInt(request.getParameter("pageNum"));
          int id = Integer.parseInt(request.getParameter("id"));
          String name = request.getParameter("name");
          int age = Integer.parseInt(request.getParameter("age"));
          String email = request.getParameter("email");
          String departmentName = request.getParameter("departmentName");
          UserDO userDO = new UserDO(id, name, age, email, departmentName);

          logger.debugRecord(Log.LOG_DUBUG, "要修改的用户信息："+userDO);

          Map<String, Object> params = new HashMap<>(16);
          params.put("action", action);
          params.put("userDO", userDO);
          //正常响应的状态码为200
          Integer successCode = 200;
          params.put("responseCode", successCode);

          try {
               params = transfer(params);
               Param param = new Param();
               boolean success = Objects.equals(successCode,params.get("responseCode"));
               if (success) {
                    param.setObject("responseCode", params.get("responseCode"));
                    //修改用户所在的页
                    String indexUrl = request.getContextPath() +
                            "/users?action=listsUser&pageNum=" + pageNum;
                    param.setObject("responseCode", params.get("responseCode"));
                    param.setObject("indexUrl", indexUrl);
                    response.getWriter().write(param.toJson());
               }
          } catch (IOException e) {
               logger.errorRecord(Log.LOG_ERROR, e,"连接失败错误，服务端ServerSocket未打开");
          } catch (ClassNotFoundException e) {
               logger.errorRecord(Log.LOG_ERROR, e,"类型找不到错误，服务端回传的类型在客户端不存在");
          } finally {
               close(socket, in ,out);

          }
     }

     /**
      * socket传输数据
      * @param params
      * @return
      * @throws IOException
      * @throws ClassNotFoundException
      */
     private Map<String, Object> transfer(Map<String, Object> params) throws IOException, ClassNotFoundException {
          InetAddress localHost = InetAddress.getLocalHost();
          socket = new Socket(localHost,8899);
          out = new ObjectOutputStream(socket.getOutputStream());
          out.writeObject(params);
          out.flush();
          in = new ObjectInputStream(socket.getInputStream());
          return (Map<String, Object>) in.readObject();
     }
}
