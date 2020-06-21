package com.stylefent.guns.entity.query;

import lombok.Data;

/**
 * @author: lx
 */
@Data
public class CommonQuery extends BaseQuery {
    private Integer type;
    private Long linkId;
    private Long linkUid;
    private String keyword;
    private String cover;
    private String[] kwArr;
    /**
     * 拓展字段
     */
    private String extra;

    private String ids;
}
