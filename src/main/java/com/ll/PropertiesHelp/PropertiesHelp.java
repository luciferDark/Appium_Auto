package com.ll.PropertiesHelp;

import com.ll.util.StringUtils;
import com.ll.util.Utils;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.*;
import java.util.*;

/**
 * 配置文件读取工具
 */
public class PropertiesHelp {
    /**
     * 打印配置文件
     *
     * @param properties
     */
    public static void printAllProperties(Properties properties) throws UnsupportedEncodingException {
        if (properties == null) {
            Utils.log("filesProperties is null");
            return;
        }
        Utils.log("filesProperties is:");
        Enumeration enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            String value = properties.getProperty(key);
            Utils.log("key : " + key + "====value : " + value);
        }
    }

    /**
     * 通过路径获取配置属性
     *
     * @param path
     * @return
     */
    public static Properties getPropertiesByPath(String path) {
        Properties pro = new Properties();
        try {
            pro = PropertiesLoaderUtils.loadAllProperties(path);
            printAllProperties(pro);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pro;
    }
    /**
     * 通过路径流获取配置属性
     *
     * @param filePath
     * @return
     */
    public static Properties getPropertiesByFileStream(String filePath) {
        Properties prop = new Properties();
        try {
            // 通过输入缓冲流进行读取配置文件
//            InputStream InputStream = new BufferedInputStream(new FileInputStream(new File(filePath)));
            InputStreamReader inputStreamReader = new InputStreamReader(new BufferedInputStream(new FileInputStream(filePath)),"GBK");
            // 加载输入流
            prop.load(inputStreamReader);
            printAllProperties(prop);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }

    /**
     * 获取配置中的某个属性
     *
     * @param path
     * @param key
     * @return
     */
    public static String getValueInPropertyByPathKey(String path, String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        String value = null;
        Properties pro = null;
        try {
            pro = PropertiesLoaderUtils.loadAllProperties(path);
            if (pro == null) {
                return null;
            }
            value = pro.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获取配置文件中某个属性
     *
     * @param pro
     * @param key
     * @return
     */
    public static String getValueInPropertyByKey(Properties pro, String key) {
        if (pro == null || StringUtils.isEmpty(key)) {
            return null;
        }
        String value = pro.getProperty(key);
        return value;
    }

    public static List<HashMap<String, String>> convertPropertiesToList(Properties properties){
        List<HashMap<String, String>> hashMapList = new ArrayList<>();
        Enumeration enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            String value = properties.getProperty(key);
            HashMap<String, String> item = new HashMap<>();
            item.put(key,value);
            hashMapList.add(item);
        }
        return hashMapList;
    }

    public static List<String> convertPropertiesValueToList(Properties properties){
        List<String> list = new ArrayList<>();
        Enumeration enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            String value = properties.getProperty(key);
            if (!list.contains(value)){
                list.add(value);
            }
        }
        return list;
    }
}

