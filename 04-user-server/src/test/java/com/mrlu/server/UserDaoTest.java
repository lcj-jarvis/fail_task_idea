package com.mrlu.server;

import com.mrlu.server.bean.UserDO;
import com.mrlu.server.dao.UserDao;
import com.mrlu.server.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * @author 简单de快乐（陆朝基）
 * @date 2021-06-07 11:59
 */
public class UserDaoTest {

    @Test
    public void listsUserDoTest(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        List<UserDO> userDOList = userDao.listsUserDo();
        userDOList.forEach(System.out :: println);
    }


    @Test
    public void insertUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        String email,name,departmentName;
        int age;
        UserDO userDO;
        LocalDateTime gmtCreate,gmtModified;
        for (int i = 0; i < 100; i++) {
            name = UUID.randomUUID().toString().substring(0, 5);
            age = i + 17;
            email = name + "@qq.com";
            departmentName = (i % 2 == 0) ? "开发部" : "测试部";
            gmtCreate = LocalDateTime.now();
            gmtModified = LocalDateTime.now();
            userDO = new UserDO(null, name ,age ,
                    email, departmentName, gmtCreate, gmtModified);
            userDao.insertUserDo(userDO);
        }
        sqlSession.commit();
    }
}
