package com.stylefent.guns.entity.enums;

import lombok.Getter;

/**
 * 牛角收支类型 0 充值 1 礼物支出
 *
 * @author: LEIYU
 */
@Getter
public enum NpRecordTypeEnum {
    Charge(0, "充值"),
    Gift(1, "礼物支出"),

    Income(0, "收入"),
    Expend(1, "支出"),

    ;

    private int code;
    private String msg;

    NpRecordTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
