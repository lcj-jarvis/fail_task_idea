package com.mrlu.client.util;

import java.util.Objects;

/**
 * @author 简单de快乐
 * @date 2021-06-09 14:59
 */
public class Log {

    public static final Integer LOG_DUBUG = 1;
    public static final Integer LOG_STANDARD = 2;
    public static final Integer LOG_ERROR = 3;

    private volatile static Log logger;

    public static Log getInstance() {
        if (logger == null) {
            synchronized (Log.class) {
                if (logger == null) {
                    logger = new Log();
                }
            }
        }
        return logger;
    }

    public void errorRecord(Integer logLevel, Throwable error, String message) {
        if (LOG_ERROR.equals(logLevel)) {
             record(error, message);
             return;
        }
        System.out.println("failure to open Log , please check the input logLevel");
    }

    public void standardRecord(Integer logLevel, String message) {
        if (LOG_STANDARD.equals(logLevel)) {
            record(null, message);
            return;
        }
        System.out.println("failure to open Log , please check the input level");
    }

    public void debugRecord(Integer logLevel, String message) {
        if (LOG_DUBUG.equals(logLevel)) {
            record(null, message);
            return;
        }
        System.out.println("failure to open Log , please check the input level");
    }

    private void record(Throwable error, String message) {
        if (error != null) {
            error.printStackTrace();
        }
        System.out.println(message);

    }
}
