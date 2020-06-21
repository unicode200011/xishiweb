package com.stylefeng.guns.rest.core.util;

import com.alibaba.fastjson.JSONObject;
import com.stylefeng.guns.core.support.HttpKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 长网址转换短网址
 *
 * @author: lx
 */
@Slf4j
public class LongUrlToShortUrl {

//    public static final String APPKEY = "5bce7034160892069f8c423349d7e12b";
    public static final String APPKEY = "d617498c292995b6fe64970d7daee6b2";
    public static final String URL = "http://www.mynb8.com/api2/sina";


    /**
     * 转换
     *
     * @param url
     * @return
     */
    public static String conversion(String url) {
        try {
            String newUrl = URLEncoder.encode(url, "UTF-8");
            String sign = DigestUtils.md5Hex(APPKEY + DigestUtils.md5Hex(newUrl).toLowerCase()).toLowerCase();
            Map<String, String> params = new HashMap<>(16);
            params.put("appkey", APPKEY);
            params.put("long_url", url);
            params.put("sign", sign);
            String resultStr = HttpKit.sendGet(URL, params);
            Map map = JSONObject.parseObject(resultStr, Map.class);
            String rsCode = map.get("rs_code").toString();
            log.info("短链接返回信息：【{}】",map);
            if ("0".equals(rsCode)) {
//                Map data = JSONObject.parseObject(map.get("data").toString(), Map.class);
                return map.get("short_url").toString();
            } else {
                return "";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        System.err.println(conversion("http://www.gamesource.cn"));
    }
}
