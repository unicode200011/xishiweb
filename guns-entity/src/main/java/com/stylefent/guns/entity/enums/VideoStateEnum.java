package com.stylefent.guns.entity.enums;

import lombok.Getter;

/**
 * 视频状态
 *
 * @author: LEIYU
 */
@Getter
public enum VideoStateEnum {
    Normal(0, "正常"),
    Forbid(1, "禁用"),
    Delete(2, "删除"),
    Draft(3, "草稿");

    private Integer code;
    private String msg;

    VideoStateEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
