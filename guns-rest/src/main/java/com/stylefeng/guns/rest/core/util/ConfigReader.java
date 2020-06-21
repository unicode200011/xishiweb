package com.stylefeng.guns.rest.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置读取工具类
 */
public class ConfigReader {

    private static final String LOCATION = "/config/conf.properties";

    private static Properties properties = null;

    static {
        try {
            InputStream input = ConfigReader.class.getResourceAsStream(LOCATION);
            properties = new Properties();
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取字符串参数
     *
     * @param param
     * @return
     */
    public static String getString(String param) {
        return properties.getProperty(param);
    }

    /**
     * 获取int参数
     *
     * @param param
     * @return
     */
    public static int getInt(String param) {
        return Integer.valueOf(properties.getProperty(param));
    }
}
