package com.stylefeng.guns.admin.modular.moneyManage.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.persistence.model.Withdrawal;
import com.stylefeng.guns.persistence.dao.WithdrawalMapper;
import com.stylefeng.guns.admin.modular.moneyManage.service.IWithdrawalService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * <p>
 * 提现 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-12-06
 */
@Service
public class WithdrawalServiceImpl extends ServiceImpl<WithdrawalMapper, Withdrawal> implements IWithdrawalService {

    @Autowired
    private WithdrawalMapper withdrawalMapper;

    @Override
    public Page selectWithdrawal(Withdrawal withdrawal, Page page) {
        List<Withdrawal> withdrawals = withdrawalMapper.selectWithdrawal(withdrawal, page);
        return page.setRecords(withdrawals);
    }
}
