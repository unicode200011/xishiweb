package com.stylefeng.guns.core.constant;

public class SystemConstants {

    public static final String COMMEN_DATA_REDIS_KEY = "xishi_commonData:key:";


    public static final String SMS_LOGIN_KEY = "xishi_PHONECODE";

    public static final String USER_REDIS_KEY = "xishi_user_redis_key_";

    /**
     * 参数加密秘钥
     */
    public static final String PARAM_KEY = "xishi_Aesnice2018";


    /**
     * 设置/找回密码操作
     */
    public static final String SMS_FIND_FIND_SET_PASS_KEY = "xishi_user_find_set_pwd_";
    /**
     * 支付密码验证redis key
     */
    public static final String PAY_PWD_REDIS_KEY_PREFIX_ = "xishi_pay_pwd_prefix_";
    public static final Long PAY_PWD_REDIS_TIME = 36000L;
    /**
     * 实名支付缓存
     */
    public static final String USER_AUTH_PAY_KEY = "user:auth:pay_";
}
