package com.mrlu.client.web;

import com.mrlu.client.util.Log;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr.Lu
 * @create 2021-01-25 23:03
 */
public abstract class BaseServlet extends HttpServlet {

    protected Socket socket;
    protected static ObjectInputStream in;
    protected static ObjectOutputStream out;
    protected static final Integer PAGE_SIZE = 5;
    protected static Log logger = Log.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    /**
     * 提取出一个抽象类，通过反射调用其他方法。
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        logger.standardRecord(Log.LOG_STANDARD,"请求到达BaseServlet");
        String action = req.getParameter("action");
        String requestUrl = req.getContextPath()+req.getRequestURI() + "?action=" + action;

        try {
            // 获取 action 业务鉴别字符串，获取相应的业务方法反射对象
            // 避免多个if分支判断造成代码冗余
            Method method = this.getClass()
                    .getDeclaredMethod(action, HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this, req, resp);
        } catch (Exception e) {
             logger.errorRecord(Log.LOG_ERROR, e,"浏览器请求错误，请求的url:"+requestUrl+"不合法");

        }
    }

    protected void close(Socket client, InputStream input, OutputStream out) {

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
