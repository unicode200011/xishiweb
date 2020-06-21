package com.stylefent.guns.entity.query;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawalQuery extends BaseQuery {


    /**
     * 真实姓名
     */
    private String userName;
    /**
     * 提现账户 支付宝账户 微信openId 银行卡ID
     */
    private String account;
    /**
     * 提现金额
     */
    private BigDecimal money;
    /**
     * 提现方式 0:支付宝 1:银行卡 2:微信
     */
    private Integer way;

}
