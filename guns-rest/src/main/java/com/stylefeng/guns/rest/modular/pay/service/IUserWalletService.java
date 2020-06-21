package com.stylefeng.guns.rest.modular.pay.service;

import com.stylefeng.guns.rest.core.util.BaseJson;
import com.stylefent.guns.entity.enums.ConsumeType;
import com.stylefent.guns.entity.enums.WalletExpendTypeEnum;
import com.stylefent.guns.entity.query.CommonQuery;
import com.stylefent.guns.entity.query.WithdrawalQuery;

import java.math.BigDecimal;

/**
 * @author: lx
 */
public interface IUserWalletService {




    /**
     * 提现
     *
     * @param query
     * @return
     */
    BaseJson withdraw(WithdrawalQuery query);

    /**
     * 提现记录
     *
     * @param query
     * @return
     */
    BaseJson withdrawRecord(CommonQuery query);







    void recordWalletDetail(ConsumeType type, Long userId, BigDecimal money, WalletExpendTypeEnum userType, String remark);


    BaseJson chargeList(CommonQuery query);
}
