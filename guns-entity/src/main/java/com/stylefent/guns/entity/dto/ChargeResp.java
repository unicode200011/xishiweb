package com.stylefent.guns.entity.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ChargeResp {
    /**
     * 充值列表id
     */
    private Long id;
    /**
     * 西施币数量
     */
    private BigDecimal xishiNum;

    /**
     * rmb数量
     */
    private BigDecimal rmb;
}
