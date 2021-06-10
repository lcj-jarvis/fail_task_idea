package com.mrlu.server.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;


/**
 * @author Mr.Lu
 * @create 2021-02-07 17:59
 */
public class MybatisUtils {

    public static SqlSessionFactory build;

    static {
        //需要和你项目的文件名一样
        String config = "mybatis.xml";
        try {
            InputStream in = Resources.getResourceAsStream(config);
            build = new SqlSessionFactoryBuilder().build(in);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public static SqlSession getSqlSession(){
        SqlSession sqlSession = null;
        if (build != null){
            //非自动提交事务的
            sqlSession = build.openSession();

        }
        return sqlSession;
    }
}
