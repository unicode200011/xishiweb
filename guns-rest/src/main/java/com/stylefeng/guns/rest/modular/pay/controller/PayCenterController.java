package com.stylefeng.guns.rest.modular.pay.controller;


import com.stylefeng.guns.rest.core.util.BaseJson;
import com.stylefeng.guns.rest.modular.pay.service.IPayCenterService;
import com.stylefent.guns.entity.query.PayQuery;
import com.stylefent.guns.entity.vo.SessionUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lx
 */
@RestController
@RequestMapping("/api/pay")
public class PayCenterController {

    @Autowired
    private IPayCenterService payCenterService;


    /**
     * @description: 支付宝支付;
     * @author: lx
     */
    @RequestMapping("/v1/aliPay")
    public BaseJson aliPay(PayQuery query, @RequestAttribute("issuer") SessionUserVo issuer) {
        query.setUserId(issuer.getUserId());
        return payCenterService.aliPay(query);
    }

    /**
     * @description: 微信支付;
     * @author: lx
     */
    @RequestMapping("/v1/wxPay")
    public BaseJson wxPay(PayQuery query, @RequestAttribute("issuer") SessionUserVo issuer, HttpServletRequest request) {
        query.setUserId(issuer.getUserId());
        return payCenterService.wxPay(query, request);
    }
}
