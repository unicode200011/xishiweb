package com.stylefeng.guns.rest.modular.auth.controller;

import com.stylefeng.guns.rest.core.util.BaseJson;
import com.stylefeng.guns.rest.modular.auth.service.IAccountService;
import com.stylefent.guns.entity.vo.AccountVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 登录
 *
 * @author lx
 */
@RestController
public class AuthController {

    @Autowired
    private IAccountService accountService;


    /**
     * @description: 账户密码登录  APP登录  loginType 0 手机号 1 三方  2 验证码
     * @author: lx
     */
    @PostMapping(value = "${jwt.auth-path}")
    public BaseJson login(@Valid AccountVo accountVo, HttpServletResponse response, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return BaseJson.ofFail(bindingResult.getFieldError().getDefaultMessage());
        }
        if (accountVo.getLoginType() == 0) {
            return accountService.login(accountVo, response, AccountVo.ACCOUNT_LOGIN);
        } else if (accountVo.getLoginType() == 1) {
            return accountService.login(accountVo, response, AccountVo.OTHER_LOGIN);
        } else {
            return accountService.login(accountVo, response, AccountVo.CODE_LOGIN);
        }
    }
    @PostMapping(value = "/logout")
    public BaseJson logout(@Valid AccountVo accountVo, HttpServletResponse response, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return BaseJson.ofFail(bindingResult.getFieldError().getDefaultMessage());
        }
        return accountService.logout(accountVo);
    }
}
