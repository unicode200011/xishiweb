package com.stylefent.guns.entity.enums;

import lombok.Getter;

/**
 * 直播状态 0:未直播 1:直播中 2:禁播
 *
 * @author: LEIYU
 */
@Getter
public enum LiveStateEnum {
    Not_Living(0, "未直播"),
    Living(1, "直播中"),
    Forbidden(2, "禁播");

    private int code;
    private String msg;

    LiveStateEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
