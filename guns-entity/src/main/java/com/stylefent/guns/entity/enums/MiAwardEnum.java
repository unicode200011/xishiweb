package com.stylefent.guns.entity.enums;

import lombok.Getter;

/**
 * 米赏
 *
 * @author: LEIYU
 */
@Getter
public enum MiAwardEnum {

    Normal(0, "正常"),
    Expire(1, "过期"),
    Settlement(2, "已到期结算中"),
    Delete(3, "删除"),
    PreAudit(0, "待审核"),
    PassAudit(1, "通过审核"),
    RefuseAudit(2, "不通过"),;

    private int code;
    private String msg;

    MiAwardEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static MiAwardEnum getByCode(int code) {
        for (MiAwardEnum payTypeEnum : values()) {
            if (payTypeEnum.getCode() == code) {
                return payTypeEnum;
            }
        }
        return null;
    }
}
