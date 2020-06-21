package com.stylefeng.guns.core.constant;

public class CommonConstants {

    public static String level(Integer level) {
        switch (level) {

            case 1:

                return "Mo主";
            case 2:

                return "艺人Mo主";
            case 3:

                return "人气Mo主";
            case 4:

                return "明星Mo主";

            default:

                return "";
        }
    }
}
