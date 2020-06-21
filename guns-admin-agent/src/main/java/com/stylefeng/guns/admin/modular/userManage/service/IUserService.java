package com.stylefeng.guns.admin.modular.userManage.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.persistence.model.User;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 用户信息 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2019-10-10
 */
public interface IUserService extends IService<User> {

    Page<User> selectAgentShowerPage(Page<User> userPage, EntityWrapper<User> entityWrapper);
}
