package com.ll.util;

public class StringUtils {
    public static boolean isEmpty(String msg) {
        if (null == msg || msg == "" || msg.length() <= 0)
            return true;
        return false;
    }
}
