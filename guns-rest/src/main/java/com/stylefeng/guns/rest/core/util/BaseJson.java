package com.stylefeng.guns.rest.core.util;


import com.alibaba.fastjson.JSONObject;
import com.stylefeng.guns.rest.constants.Biz;

import java.io.Serializable;

public class BaseJson<T> implements Serializable {

    private T result;
    private String code;
    private String msg;
    private String time;

    @SuppressWarnings(value = "unchecked")
    public BaseJson(T result, String code, String msg) {
        this.result = result;
        if ("".equals(result)) this.result = (T) new JSONObject();
        this.code = code;
        this.msg = msg;
        this.time = String.valueOf(System.currentTimeMillis() / 1000);
    }

    @SuppressWarnings(value = "unchecked")
    public BaseJson(T result, Biz biz, String msg) {
        this.result = result;
        if ("".equals(result)) this.result = (T) new JSONObject();
        this.code = biz.getCode();
        this.msg = msg;
        this.time = String.valueOf(System.currentTimeMillis() / 1000);
    }

    public static BaseJson ofSuccess(Object result) {
        return new BaseJson(result, Biz.OK, "");
    }

    public static BaseJson ofSuccess(Object result, String msg) {
        return new BaseJson(result, Biz.OK, msg);
    }

    public static BaseJson ofSuccess() {
        return new BaseJson("", Biz.OK, "");
    }

    public static BaseJson ofSuccess(String msg) {
        return new BaseJson("", Biz.OK, msg);
    }

    public static BaseJson ofFail(String message) {
        return new BaseJson("", Biz.OP_ERROR, message);
    }

    public static BaseJson ofFail(String message, Object object) {
        return new BaseJson(object, Biz.OP_ERROR, message);
    }

    public static BaseJson ofFail(Biz biz, Object data) {
        return new BaseJson(data, biz, biz.getSign());
    }

    public static BaseJson invalidParam() {
        return ofFail(Biz.INVALID_PARAM);
    }

    public static BaseJson ofFail(Biz message) {
        return new BaseJson("", message.getCode(), message.getSign());
    }

    public static BaseJson notFound(String message) {
        return new BaseJson("", Biz.OP_ERROR, message);
    }

    public static BaseJson userNotExist() {
        return new BaseJson("", Biz.USER_NOT_EXIST, Biz.USER_NOT_EXIST.getSign());
    }

    public static BaseJson fixing() {
        return new BaseJson("", "502", "系统维护中,请稍等再试");
    }

    public BaseJson(String msg) {
        this.result = (T) "";
        this.code = "500";
        this.msg = msg;
        this.time = String.valueOf(System.currentTimeMillis() / 1000);
    }

    public BaseJson(String code, String msg) {
        this.result = (T) "";
        this.code = code;
        this.msg = msg;
        this.time = String.valueOf(System.currentTimeMillis() / 1000);
    }

    public T getData() {
        return result;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getTime() {
        return time;
    }

}
