package com.stylefent.guns.entity.enums;

import lombok.Getter;

/**
 * 视频事件类型
 *
 * @author leiyu
 */
@Getter
public enum VideoEventTypeEnum {
    /**
     * 转码
     */
    Transcode("Transcode");

    private String sign;

    VideoEventTypeEnum(String sign) {
        this.sign = sign;
    }

    public static VideoEventTypeEnum getType(String type) {
        for (VideoEventTypeEnum videoEventTypeEnum : values()) {
            if (videoEventTypeEnum.getSign().equals(type)) {
                return videoEventTypeEnum;
            }
        }
        return null;
    }
}
