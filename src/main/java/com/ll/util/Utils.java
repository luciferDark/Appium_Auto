package com.ll.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss..SSSS");

    public static void log(String msg) {
        String logMsg = format.format(new Date(System.currentTimeMillis())) + " :\t";
        try {
            String msgUTF8 = new String(msg.getBytes(), "UTF-8");
            logMsg += msgUTF8;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(logMsg);
    }
}
