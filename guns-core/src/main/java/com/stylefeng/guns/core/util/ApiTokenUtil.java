package com.stylefeng.guns.core.util;

import com.stylefeng.guns.core.constant.RedisConstants;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.support.StrKit;
import com.stylefeng.guns.core.util.redis.RedisService;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 *接口幂等性防止重复提交token工具类
 */
public class ApiTokenUtil {
    private static final  String TOKEN_NAME = "token";
    private  static RedisService redisService = SpringContextHolder.getBean(RedisService.class);
    public static String createToken(){
        String token = UUID.randomUUID().toString().replaceAll("\\-","");
        redisService.set(RedisConstants.TOKEN_PREFIX+token,token,3600000L);
        return token;
    }

    public static boolean removeToken(String token){
          return  redisService.removeAndReturn(RedisConstants.TOKEN_PREFIX+token);
    }

    public static void checkToken(HttpServletRequest request){
        String token = request.getHeader(TOKEN_NAME);
        if(StrKit.isEmpty(token)){
            String parameter = request.getParameter(TOKEN_NAME);
            if(StrKit.isEmpty(parameter)){
                throw  new GunsException(413,"不合法的请求");
            }
            token = parameter;
        }
        if(!redisService.exists(RedisConstants.TOKEN_PREFIX+token)){
            throw  new GunsException(413,"信息已提交，切勿重复提交");
        }
        boolean b = ApiTokenUtil.removeToken(token);
        if(!b){
            throw  new GunsException(413,"切勿重复提交");
        }
    }
}
