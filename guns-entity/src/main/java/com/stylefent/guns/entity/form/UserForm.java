package com.stylefent.guns.entity.form;

import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用户
 *
 * @author: LEIYU
 */
@Setter
public class UserForm {

    private Long id;
    /**
     * 昵称
     */
    private String name;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 性别: 0:男 1:女  2:保密
     */
    private Integer gender;
    /**
     * 个人简介
     */
    private String intro;
    /**
     * 支付宝账户
     */
    private String aliPay;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 生日
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 邀请码
     */
    private String code;

    /**
     * 银行卡与卡号
     */
    private String bankCardNo;

    private String bank;


    public Long getId() {
        return id;
    }

    public String getName() {
        return StringUtils.isEmpty(name) ? "" : name;
    }

    public String getAvatar() {
        return StringUtils.isEmpty(avatar) ? "" : avatar;
    }

    public Integer getGender() {
        return gender == null || gender > 2 ? 0 : gender;
    }

    public String getIntro() {
        return StringUtils.isEmpty(intro) ? "" : intro;
    }

    public String getAliPay() {
        return StringUtils.isEmpty(aliPay) ? "" : aliPay;
    }

    public String getRealName() {
        return StringUtils.isEmpty(realName) ? "" : realName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getCode() {
        return code;
    }

    public String getBankCardNo() {
        return StringUtils.isEmpty(bankCardNo) ? "" : bankCardNo;
    }

    public String getBank() {
        return StringUtils.isEmpty(bank) ? "" : bank;
    }
}
