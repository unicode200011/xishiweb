package com.stylefeng.guns.admin.modular.system.transfer;

import java.util.Date;

/**
 * 管理员的信息封装
 *
 * @author fengshuonan
 * @Date 2017年1月11日 下午7:46:53
 */
public class ManagerUser {

    private String userId;

    /* 用户账号 */
    private String userNo;

    /* 用户姓名 */
    private String userName;

    private String userPhone;

    //1:超级管理员  2：管理员
    private String userRole;

    /* 1：登录状态 2：退出状态 3：停用状态 */
    private Integer userStatus;

    private Date createTime;

    private Date loginTime;


}
