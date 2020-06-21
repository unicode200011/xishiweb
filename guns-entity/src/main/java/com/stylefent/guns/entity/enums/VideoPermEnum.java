package com.stylefent.guns.entity.enums;

import lombok.Getter;

/**
 * 视频权限
 *
 * @author: LEIYU
 */
@Getter
public enum VideoPermEnum {
    Public(0, "公开"),
    Friend(1, "好友可见"),
    Private(2, "私密");

    private Integer code;
    private String msg;

    VideoPermEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static VideoPermEnum getByCode(Integer code) {
        for (VideoPermEnum videoPermEnum : values()) {
            if (videoPermEnum.getCode() == code) {
                return videoPermEnum;
            }
        }
        return null;
    }
}
