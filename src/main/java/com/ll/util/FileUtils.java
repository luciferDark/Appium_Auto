package com.ll.util;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    public static String getResRoot(){
        File directory = new File("");// 参数为空
        String fileUrl = null;
        try {
            fileUrl = directory.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    public static String getResRoot1(){
        String filepath=System.getProperty("user.dir");
        String fileUrl = filepath + File.separator + "res";
        return fileUrl;
    }
}
