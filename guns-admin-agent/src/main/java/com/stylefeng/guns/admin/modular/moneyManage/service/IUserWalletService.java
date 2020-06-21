package com.stylefeng.guns.admin.modular.moneyManage.service;

import com.stylefeng.guns.persistence.model.UserWallet;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 用户账户 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2019-12-06
 */
public interface IUserWalletService extends IService<UserWallet> {
    UserWallet selectByUserId(Long id);
}
