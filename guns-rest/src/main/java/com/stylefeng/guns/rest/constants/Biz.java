package com.stylefeng.guns.rest.constants;

import lombok.Getter;

/**
 * 业务类型
 *
 * @author lx
 */

@Getter
public enum Biz {

    OK("200", "操作成功"),
    OP_ERROR("10000", "操作错误"),
    INVALID_PARAM("10000", "参数错误"),
    USER_NOT_EXIST("10001", "用户不存在或状态异常"),
    REGISTER("10002", "请先注册"),
    BANDING_PHONE("10003", "绑定手机号"),
    SET_PAY_PWD("10004", "请设置支付密码"),
    MONEY_NOT_ENOUGH("10005", "余额不足"),
    NOT_REGISTER("10006", "该手机号未注册"),
    AUTH_NOT_PAY("10007", "请先支付"),
    PHONE_ERROR("10009", "手机号格式错误"),
    INTERNET_ERROR("10010", "网络繁忙，请稍后再试"),
    PHONE_PWD_ERROR("10011", "用户名或密码不正确"),
    HAD_REGIST("10008", "该手机号已注册");


    String code;
    String sign;

    Biz(String code, String sign) {
        this.code = code;
        this.sign = sign;
    }
}
