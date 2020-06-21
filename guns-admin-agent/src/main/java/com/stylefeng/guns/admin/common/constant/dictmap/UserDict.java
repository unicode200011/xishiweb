package com.stylefeng.guns.admin.common.constant.dictmap;

import com.stylefeng.guns.admin.common.constant.dictmap.base.AbstractDictMap;

/**
 * Created by hejia on 2018/3/16.
 */
public class UserDict extends AbstractDictMap {
    @Override
    public void init() {
        put("userId","账号");
        put("login_name","登录名");
        put("phone","电话");
        put("name","昵称");
        put("avatar","用户头像");
        put("email","邮箱");
    }

    @Override
    protected void initBeWrapped() {

    }
}
