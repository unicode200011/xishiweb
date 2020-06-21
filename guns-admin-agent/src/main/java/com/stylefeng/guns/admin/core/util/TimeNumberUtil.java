package com.stylefeng.guns.admin.core.util;

/**
 * Created by hejia on 2018/4/26.
 */
public class TimeNumberUtil {

    public static void main(String[] args) {
        String s = String.valueOf(System.currentTimeMillis()).substring(0, 10);
        int i = Integer.parseInt(s);
        System.out.println(i);
    }

    public static String getNumberNo() {
        return String.valueOf(System.currentTimeMillis()).substring(1, 13);
    }
    public static Integer getNumberNoInt() {
        String s = String.valueOf(System.currentTimeMillis()).substring(0, 10);
        int i = Integer.parseInt(s);
        return i;
    }
}
