package com.stylefeng.guns.rest.modular.user.controller;


import com.stylefeng.guns.core.constant.SystemConstants;
import com.stylefeng.guns.core.intercepter.ApiIdempotent;
import com.stylefeng.guns.core.util.redis.RedisService;
import com.stylefeng.guns.rest.common.annotatoin.RequestLimit;
import com.stylefeng.guns.rest.common.aop.RequestLimitHandler;
import com.stylefeng.guns.rest.config.system.SystemConfig;
import com.stylefeng.guns.rest.constants.Limit;
import com.stylefeng.guns.rest.core.util.BaseJson;
import com.stylefeng.guns.rest.modular.auth.service.IAccountService;
import com.stylefent.guns.entity.vo.AccountVo;
import com.stylefent.guns.entity.vo.BingdingPhoneVo;
import com.stylefent.guns.entity.vo.SessionUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author lx
 */
@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {

    @Autowired
    private IAccountService accountService;




    /**
     * @description: 注册;
     * @author: lx
     */
    @PostMapping("/register")
    @ApiIdempotent
    public BaseJson register(@Valid AccountVo accountVo, BindingResult bindingResult,HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return BaseJson.ofFail(bindingResult.getFieldError().getDefaultMessage());
        }
        String ipAddress = RequestLimitHandler.getIpAddress(request);
        log.info("【IP拦截】用户真实IP=【{}】，用户手机号=【{}】", ipAddress, accountVo.getAccount());
        return accountService.register(accountVo);
//        return BaseJson.ofFail("系统维护中");
    }
    /**
     * @description: 重置密码
     * @author: lx
     */
    @PostMapping("/resetPwd")
    public BaseJson resetPwd(@Valid AccountVo accountVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return BaseJson.ofFail(bindingResult.getFieldError().getDefaultMessage());
        }
        return accountService.resetPwd(accountVo);
    }

    /**
     * @description: 发送验证码
     * @author: lx
     */
    @PostMapping("/sms")
//    @RequestLimit(limit = Limit.TIME_OF_MINUTES, count = 1, msg = "一分钟内只能发送一次短信")
    public BaseJson send(@Valid AccountVo accountVo, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return BaseJson.ofFail(bindingResult.getFieldError().getDefaultMessage());
        }
        String ipAddress = RequestLimitHandler.getIpAddress(request);
        log.info("【IP拦截】用户真实IP=【{}】，用户手机号=【{}】", ipAddress, accountVo.getAccount());
        return accountService.send(accountVo);
    }

    /**
     * @description: 验证验证码
     * @author: lx
     */
    @PostMapping("/smsValidate")
    public BaseJson smsValidate(@Valid AccountVo accountVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return BaseJson.ofFail(bindingResult.getFieldError().getDefaultMessage());
        }
        return accountService.smsValidate(accountVo);
    }




}
