package com.stylefent.guns.entity.enums;

import lombok.Getter;

/**
 * 牛角商品状态及交易方式
 *
 * @author: LEIYU
 */
@Getter
public enum NjGoodsStateEnum {

    Selling(0, "挂卖中"),
    Trading(1, "交易中"),
    Finished(2, "已完成"),
    Cancel(3, "已取消"),

    Sell(0, "卖(转出)"),
    Buy(1, "买(转入)"),;


    private int code;
    private String sign;

    NjGoodsStateEnum(int code, String sign) {
        this.code = code;
        this.sign = sign;
    }
}
