package com.stylefeng.guns.rest.common.aop;

import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.core.exception.RequestLimitException;
import com.stylefeng.guns.core.support.StrKit;
import com.stylefeng.guns.core.util.redis.RedisService;
import com.stylefeng.guns.rest.common.annotatoin.RequestLimit;
import com.stylefeng.guns.rest.constants.Limit;
import com.stylefent.guns.entity.vo.SessionUserVo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 请求限制处理
 *
 * @author: lx
 */
@Aspect
@Component
@Slf4j
public class RequestLimitHandler {

    private static final String PREFIX_IP = "req:lim:ip_";
    private static final String PREFIX_MACHINE = "req:lim:mac_";
    private static final String PREFIX_USER_ID = "req:lim:uid_";

    @Autowired
    private RedisService redisService;

    @Before("within(@org.springframework.web.bind.annotation.RestController *) && @annotation(limit)")
    public void limit(RequestLimit limit) throws RequestLimitException {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String ip = getIpAddress(request);
            String url = request.getServletPath().toString();
            String key = PREFIX_IP.concat(url).concat("_").concat(ip);
            if (limit.limit().equals(Limit.MACHINE)) {
                String machine = request.getParameter("machine");
                if (StrKit.isEmpty(machine)) {
                    machine = request.getParameter("account");
                }
                if (StrKit.isNotEmpty(machine)) {
                    key = PREFIX_MACHINE.concat(url).concat("_").concat(machine);
                } else {
                    String userIdStr = request.getParameter("userId");
                    if (StrKit.isNotEmpty(userIdStr)) {
                        Long userId = Long.valueOf(userIdStr);
                        if (userId > 0) {
                            key = PREFIX_USER_ID.concat(url).concat("_").concat(userIdStr);
                        }
                    }
                }
            }
            //是否使用token参数
            if (limit.tokenAttr()) {
                Object attribute = request.getAttribute(limit.tokenAttrName());
                if (attribute != null) {
                    SessionUserVo userVo = (SessionUserVo) attribute;
                    key = PREFIX_USER_ID.concat(url).concat("_").concat(userVo.getUserId().toString());
                }
            }
            boolean result = checkRequest(limit, key);
            if (!result) {
                log.info("请求【{}】被限制", key);
                throw new RequestLimitException(limit.msg());
            }
        } catch (RequestLimitException e) {
            throw e;
        }
    }

    private boolean checkRequest(RequestLimit limit, String key) {
        Limit type = limit.limit();
        int count = limit.count();

        if (type.equals(Limit.TIME_OF_SECOND)) {
            return checkRedisForTime(key, count, TimeUnit.SECONDS);

        } else if (type.equals(Limit.TIME_OF_MINUTES)) {
            return checkRedisForTime(key, count, TimeUnit.MINUTES);

        } else if (type.equals(Limit.TIME_OF_HOUR)) {
            return checkRedisForTime(key, count, TimeUnit.HOURS);

        } else if (type.equals(Limit.TIME_OF_DAY)) {
            return checkRedisForTime(key, count, TimeUnit.DAYS);

        } else if (type.equals(Limit.MACHINE)) {
            return checkRedisForTime(key, count, TimeUnit.SECONDS);

        } else {
            return checkRedisForCount(key, limit.interval(), limit.count());
        }
    }

    /**
     * @description: 处理 限定时间限定请求次数(限定时间内可以请求指定次数)
     * @author: lx
     */
    private boolean checkRedisForCount(String key, long time, int count) {
        Long expireTime = redisService.getExpireByKey(key);
        Long iCount = this.redisService.incrAndDecr(key, 1L);
        if (iCount == 1) {
            this.redisService.setExpireKey(key, time, TimeUnit.SECONDS);
        }
        if (iCount > count) {
            if (expireTime != null && expireTime > 0) {
                this.redisService.setExpireKey(key, expireTime, TimeUnit.SECONDS);
            }
            return false;
        }
        return true;
    }

    /**
     * @description: 处理 限定时间(限定时间内只能请求一次)
     * @author: lx
     */
    private boolean checkRedisForTime(String key, int count, TimeUnit timeUnit) {
        Object o = this.redisService.get(key);
        if (o == null) {
            this.redisService.set(key, 1);
            this.redisService.setExpireKey(key, Long.valueOf(count), timeUnit);
            return true;
        } else {
            return false;
        }
    }


    /**
     * @description: 获取IP;
     * @author: lx
     */
    public final static String getIpAddress(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, Object> headers = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            String value = request.getHeader(header);
            headers.put(header, value);
        }
        log.info("请求头【{}】", JSON.toJSONString(headers));
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("x-real-ip");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("X-Real-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

}
