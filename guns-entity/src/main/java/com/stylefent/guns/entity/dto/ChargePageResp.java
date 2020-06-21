package com.stylefent.guns.entity.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ChargePageResp {
    /**
     * 当前余额
     */
    private BigDecimal amount;
    /**
     * 汇率
     */
    private BigDecimal rate;
    /**
     * 充值列表
     */
    private List<ChargeResp> chargeResps;
}
