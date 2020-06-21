package com.stylefent.guns.entity.enums;

import lombok.Getter;

/**
 * @author: LEIYU
 */
@Getter
public enum ConsumeType {

    Recharge(0, "充值"),
    WithDraw(1, "提现"),
    ChargeNp(2, "西施币充值");

    private int code;
    private String sign;

    ConsumeType(int code, String sign) {
        this.code = code;
        this.sign = sign;
    }
}
