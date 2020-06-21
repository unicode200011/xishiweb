package com.stylefeng.guns.admin.modular.showerManage.service.impl;

import com.stylefeng.guns.persistence.model.UserAuthInfo;
import com.stylefeng.guns.persistence.dao.UserAuthInfoMapper;
import com.stylefeng.guns.admin.modular.showerManage.service.IUserAuthInfoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户实名认证信息 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-11-27
 */
@Service
public class UserAuthInfoServiceImpl extends ServiceImpl<UserAuthInfoMapper, UserAuthInfo> implements IUserAuthInfoService {

}
