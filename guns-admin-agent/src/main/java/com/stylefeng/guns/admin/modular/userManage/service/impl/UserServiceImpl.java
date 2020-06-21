package com.stylefeng.guns.admin.modular.userManage.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.persistence.model.User;
import com.stylefeng.guns.persistence.dao.UserMapper;
import com.stylefeng.guns.admin.modular.userManage.service.IUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-10-10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public Page<User> selectAgentShowerPage(Page<User> userPage, EntityWrapper<User> entityWrapper) {

        return userPage.setRecords(baseMapper.selectAgentShowerPage(userPage,entityWrapper));
    }
}
