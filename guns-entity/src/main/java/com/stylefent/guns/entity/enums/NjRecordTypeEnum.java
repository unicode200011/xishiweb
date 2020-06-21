package com.stylefent.guns.entity.enums;

import lombok.Getter;

/**
 * 牛角记录类型
 *
 * @author: LEIYU
 */
@Getter
public enum NjRecordTypeEnum {

    Receive(0, "领取"),
    BuyGoods(1, "购买商品"),
    Sell(2, "转出"),
    Buy(3, "转入"),
    SellBack(4, "转出退回"),

    Income(0, "收入"),
    Expend(1, "支出"),;


    private int code;
    private String sign;

    NjRecordTypeEnum(int code, String sign) {
        this.code = code;
        this.sign = sign;
    }
}
