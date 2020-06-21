package com.stylefent.guns.entity.enums;

import lombok.Getter;

/**
 * 用户钱包
 *
 * @author: LEIYU
 */
@Getter
public enum UserWalletEnum {
    Income(0, "收入"),
    Expend(1, "支出"),
    NormalPerson(0, "普通用户"),
    PaiPerson(1, "拍客"),
    Sun(0, "阳光值"),
    UnlockTicket(1, "解锁券"),;


    private int code;
    private String msg;


    UserWalletEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
