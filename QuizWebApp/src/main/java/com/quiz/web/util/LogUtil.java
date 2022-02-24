package com.quiz.web.util;

public class LogUtil {

    // Private Constructor
    private LogUtil() {
    }

    // print line
    public static void log(String tag, String log) {
        System.out.println(tag + " : " + log);
    }

}
