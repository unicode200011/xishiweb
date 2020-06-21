package com.stylefent.guns.entity.enums;

import lombok.Getter;

/**
 * 视频评论支付类型
 *
 * @author: LEIYU
 */
@Getter
public enum VideoCommentPayTypeEnum {
    Money(0, "现金"),
    Coupon(1, "优惠券"),
    Not(2, "无需"),;

    private Integer code;
    private String msg;

    VideoCommentPayTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static VideoCommentPayTypeEnum getByCode(Integer code) {
        for (VideoCommentPayTypeEnum videoCommentPayTypeEnum : values()) {
            if (videoCommentPayTypeEnum.getCode().equals(code)) {
                return videoCommentPayTypeEnum;
            }
        }
        return null;
    }
}
