package com.stylefent.guns.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: lx
 */
@Data
public class AccountVo {

    /**
     * 账户密码登录
     */
    public static final int ACCOUNT_LOGIN = 0;
    /**
     * 三方登录
     */
    public static final int OTHER_LOGIN = 1;
    /**
     * 验证码登录
     */
    public static final int CODE_LOGIN = 2;

    private String account;
    private String password;
    private String code;
    private String key;
    private Long userId;
    /**
     * 登录模式下: 0:qq 1:微信  / 短信下表示: 0:注册 1:修改密码绑定等已注册后的操作
     */
    private int type = 0;
    /**
     * 登录方式 0 手机号 1 三方
     */
    private int loginType = 0;

    /**
     * 城市
     */
    private String city;

    //省份
    private String province;

    /**
     * 经度
     */
    private BigDecimal lgt;
    /**
     * 纬度
     */
    private BigDecimal lat;
    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 后台参数
     */
    private String param;
    /**
     * 注册类型 0:注册 1:后台添加
     */
    private int regType = 0;
    /**
     * 拓展字段
     */
    private String extra;

    private String oldPwd;

    //=========注册信息=========
    /**
     * 2：女1：男
     */
    private int gender;
    /**
     * 昵称
     */
    private String name;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 生日日期
     */
    private String birthday;

}
