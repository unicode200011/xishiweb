package com.stylefeng.guns.rest.modular.auth.validator.impl;

import com.stylefeng.guns.core.util.MapUtil;
import com.stylefeng.guns.persistence.dao.UserMapper;
import com.stylefeng.guns.rest.config.properties.JwtProperties;
import com.stylefeng.guns.rest.core.util.BaseJson;
import com.stylefeng.guns.rest.core.util.JwtTokenUtil;
import com.stylefeng.guns.rest.modular.auth.validator.IReqValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 账号密码验证
 *
 * @author fengshuonan
 * @date 2017-08-23 12:34
 */
@Service
public class DbValidator implements IReqValidator {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public BaseJson validate(Map body, HttpServletResponse response) {
        // TODO 处理用户登录操作

        //存入用户TOKEN
        final String randomKey = jwtTokenUtil.getRandomKey();
        final String token = jwtTokenUtil.generateToken(String.valueOf("18123362419"), 1l, randomKey);
        response.setHeader(this.jwtProperties.getTokenKey(), token);
        return BaseJson.ofSuccess(MapUtil.build().put("userId", 1l).over());
    }
}
