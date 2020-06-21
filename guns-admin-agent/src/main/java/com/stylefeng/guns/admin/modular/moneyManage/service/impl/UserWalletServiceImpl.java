package com.stylefeng.guns.admin.modular.moneyManage.service.impl;

import com.stylefeng.guns.persistence.model.UserWallet;
import com.stylefeng.guns.persistence.dao.UserWalletMapper;
import com.stylefeng.guns.admin.modular.moneyManage.service.IUserWalletService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户账户 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-12-06
 */
@Service
public class UserWalletServiceImpl extends ServiceImpl<UserWalletMapper, UserWallet> implements IUserWalletService {

    @Override
    public UserWallet selectByUserId(Long id) {
        return baseMapper.selectByUserId(id);
    }
}
