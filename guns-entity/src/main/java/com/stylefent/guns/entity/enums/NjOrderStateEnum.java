package com.stylefent.guns.entity.enums;

import lombok.Getter;

/**
 * 牛角商品状态及交易方式
 *
 * @author: LEIYU
 */
@Getter
public enum NjOrderStateEnum {

    NotPay(0, "待付款"),
    Paid(1, "已付款"),
    Finished(2, "已完成"),
    Cancel_Normal(3, "已取消(正常)"),
    Cancel_Plat(4, "已取消(平台介入)"),

    Sell(0, "卖(转出)"),
    Buy(1, "买(转入)"),;


    private int code;
    private String sign;

    NjOrderStateEnum(int code, String sign) {
        this.code = code;
        this.sign = sign;
    }
}
