package com.stylefent.guns.entity.enums;

import lombok.Getter;

/**
 * 用户礼券使用类型
 *
 * @author: LEIYU
 */
@Getter
public enum GiftCouponsEnum {
    VideoUnlock(0, "unlock"),
    VideoComment(1, "comment"),
    GiftChat(2, "giftTalk"),
    Back(0, "退回"),
    Use(1, "使用"),;

    private Integer code;
    private String msg;

    GiftCouponsEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
