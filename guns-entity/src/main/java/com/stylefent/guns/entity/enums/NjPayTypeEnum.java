package com.stylefent.guns.entity.enums;

import lombok.Getter;

/**
 * 牛角收支类型
 *
 * @author: LEIYU
 */
@Getter
public enum NjPayTypeEnum {

    Receive(0, "领取"),
    BuyGoods(1, "购买商品"),
    RollOut(2, "转出"),
    RollIn(3, "转入"),;

    private int code;
    private String sign;

    NjPayTypeEnum(int code, String sign) {
        this.code = code;
        this.sign = sign;
    }
}
