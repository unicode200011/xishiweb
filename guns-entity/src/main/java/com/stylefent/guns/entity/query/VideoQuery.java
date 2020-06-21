package com.stylefent.guns.entity.query;

import lombok.Data;

/**
 * @author: LEIYU
 */
@Data
public class VideoQuery extends BaseQuery {
    private Long videoId;
    private String machine;
    private String videoIds;
    private String keyword;
    private String typeKw;
}
