package com.stylefent.guns.entity.enums;

import lombok.Getter;

/**
 * 视频审核状态 0:未审核 1:通过 2:不通过
 *
 * @author: LEIYU
 */
@Getter
public enum VideoAuditEnum {
    PreAudit(0, "未审核"),
    Pass(1, "通过"),
    Refuse(2, "不通过");

    private Integer code;
    private String msg;

    VideoAuditEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
