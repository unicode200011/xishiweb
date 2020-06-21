package com.stylefeng.guns.rest.modular.pay.service;

import com.stylefeng.guns.rest.core.util.BaseJson;
import com.stylefent.guns.entity.query.PayQuery;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IPayCenterService {
    BaseJson aliPay(PayQuery query);

    BaseJson wxPay(PayQuery query, HttpServletRequest request);

    void aliNotify(HttpServletRequest request, HttpServletResponse response);

    void wxNotify(HttpServletRequest request, HttpServletResponse response);
}
