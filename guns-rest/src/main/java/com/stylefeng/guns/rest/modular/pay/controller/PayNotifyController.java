package com.stylefeng.guns.rest.modular.pay.controller;

import com.stylefeng.guns.rest.modular.pay.service.IPayCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lx
 */
@Controller
public class PayNotifyController {

    @Autowired
    IPayCenterService payCenterService;

    /**
     * @description: 支付宝回调
     * @author: lx
     */
    @RequestMapping("/rlbAliPayNotify")
    public void aliNotify(HttpServletRequest request, HttpServletResponse response) {
        this.payCenterService.aliNotify(request,response);
    }

    /**
     * @description: 微信回调
     * @author: lx
     */
    @RequestMapping("/rlbWxPayNotify")
    public void wxNotify(HttpServletRequest request, HttpServletResponse response) {
        this.payCenterService.wxNotify(request,response);
    }
}
