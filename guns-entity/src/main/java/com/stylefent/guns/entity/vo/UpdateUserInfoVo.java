package com.stylefent.guns.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: lx
 */
@Data
public class UpdateUserInfoVo {

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

    private String intro;

    private String province;

    private String city;

    private Integer age;

    private String  job;

    private Long userId;

}
