package com.stylefent.guns.entity.query;

import lombok.Data;

@Data
public class LiveQuery extends BaseQuery {
    private Long liveId;
    private Long recordId;
    private Long otherId;
    private String name;
    private Integer count;
    private Integer adminId;

    /**
     * 直播预设
     */
    private String desc;
    private String time;
    private Integer opType;
}
