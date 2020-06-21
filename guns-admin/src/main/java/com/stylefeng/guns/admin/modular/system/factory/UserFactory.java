package com.stylefeng.guns.admin.modular.system.factory;

import com.stylefeng.guns.admin.modular.system.transfer.AdminDto;
import com.stylefeng.guns.persistence.model.Admin;
import org.springframework.beans.BeanUtils;

/**
 * 用户创建工厂
 *
 * @author fengshuonan
 * @date 2017-05-05 22:43
 */
public class UserFactory {

    public static Admin createUser(AdminDto adminDto) {
        if (adminDto == null) {
            return null;
        } else {
            Admin admin = new Admin();
            BeanUtils.copyProperties(adminDto, admin);
            return admin;
        }
    }
}
