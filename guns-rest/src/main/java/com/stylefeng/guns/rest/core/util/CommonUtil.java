package com.stylefeng.guns.rest.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.constant.SystemConstants;
import com.stylefeng.guns.core.support.HttpKit;
import com.stylefeng.guns.core.util.MD5Util;
import com.stylefeng.guns.core.util.SpringContextHolder;
import com.stylefeng.guns.core.util.redis.RedisService;
import org.redisson.misc.Hash;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

/**
 * rest 工具类
 *
 * @author: lx
 */
public class CommonUtil {

    /**
     * 对象 2 Json 2 byte 数组
     *
     * @param object
     * @return
     */
    public static byte[] objectToByteArray(Object object) {
        return JSON.toJSONString(object).getBytes();
    }

    /**
     * byte 数组 2 对象
     *
     * @param byteArray
     * @param clz
     * @return
     */
    public static Object byteArrayToObject(byte[] byteArray, Class clz) {
        return JSONObject.parseObject(new String(byteArray), clz);
    }





    /**
     * 获取封面宽高
     *
     * @param url
     * @return
     */
    public static int[] getWidthAndHeight(String url) {
        try {
            InputStream is = new URL(url).openStream();
            BufferedImage sourceImg = ImageIO.read(is);
            int width = sourceImg.getWidth();
            int height = sourceImg.getHeight();
            return new int[]{width, height};
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new int[]{720, 1280};
    }

}
