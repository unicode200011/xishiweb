package com.stylefeng.guns.admin.modular.moneyManage.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.persistence.model.Withdrawal;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 提现 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2019-12-06
 */
public interface IWithdrawalService extends IService<Withdrawal> {
    public Page selectWithdrawal(Withdrawal withdrawal, Page page);
}
