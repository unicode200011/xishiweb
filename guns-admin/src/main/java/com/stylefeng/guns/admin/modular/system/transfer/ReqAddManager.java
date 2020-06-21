package com.stylefeng.guns.admin.modular.system.transfer;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 添加管理员的请求bean
 *
 * @author fengshuonan
 * @Date 2017年1月12日 下午6:46:24
 */
public class ReqAddManager {

    // 用户姓名
    @NotNull
    private String userName;

    // 用户账号
    @NotNull
    private String userNo;

    // 手机号
    @NotNull
    @Length(min = 11, max = 11)
    private String userPhone;

    // 1:超级管理员 2：管理员
    @NotNull
    private String userRole;

    // 密码
    @NotNull
    private String userPassword;

}
