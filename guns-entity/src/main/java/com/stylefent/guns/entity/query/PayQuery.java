package com.stylefent.guns.entity.query;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PayQuery extends BaseQuery {

    private BigDecimal count;

    /**
     * 0 充值  1 实名
     */
    private int payType = 0;

    private Integer chargeId;

}
