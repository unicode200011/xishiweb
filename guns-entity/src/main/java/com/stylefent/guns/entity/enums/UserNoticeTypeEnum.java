package com.stylefent.guns.entity.enums;

import lombok.Getter;

/**
 * 用户通知类型
 *
 * @author: LEIYU
 */
@Getter
public enum UserNoticeTypeEnum {
    Praise(0, "点赞"),
    Comment(1, "文字评论"),
    Video_Comment(2, "视频评论"),
    Reply_Comment(3, "回复评论"),;

    private Integer code;
    private String msg;

    UserNoticeTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static UserNoticeTypeEnum getByCode(Integer code) {
        for (UserNoticeTypeEnum videoPermEnum : values()) {
            if (videoPermEnum.getCode() == code) {
                return videoPermEnum;
            }
        }
        return null;
    }
}
