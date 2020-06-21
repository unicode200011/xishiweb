package com.stylefeng.guns.core.util;

import org.apache.commons.codec.binary.Hex;

/**
 * Created by hejia on 2018/3/15.
 */
public class MD5EncodeUtil {

    /**
     * @Author : hejk
     * @Description :加密密码
     * @params password 明密码
     * @Date 2018/3/15 13:26
     */
    public static String encodePassword(String password, String salt){
        byte[] hashPwd = Digests.sha1(password.getBytes(), salt.getBytes(), 1024);

        return Hex.encodeHexString(hashPwd);
    }

    public static byte[] getSalt(){
        return Digests.generateSalt(5);
    }
}
