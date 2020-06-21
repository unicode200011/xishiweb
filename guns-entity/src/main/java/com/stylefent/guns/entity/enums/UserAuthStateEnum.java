package com.stylefent.guns.entity.enums;

import lombok.Getter;

/**
 * @author: LEIYU
 */
@Getter
public enum UserAuthStateEnum {

    Pre(0, "待实名"),
    Ing(1, "认证中"),
    Pass(2, "认证通过"),
    Refuse(3, "认证失败"),;

    private int code;
    private String sign;

    UserAuthStateEnum(int code, String sign) {
        this.code = code;
        this.sign = sign;
    }
}
