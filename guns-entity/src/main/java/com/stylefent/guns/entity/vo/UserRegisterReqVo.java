package com.stylefent.guns.entity.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by root on 9/12/18.
 */
@Setter
@Getter
@ToString
public class UserRegisterReqVo {
    /**
     * 0：女1：男
     */
    private int gender;

    private String phone;

    private String password;
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
