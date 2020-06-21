package com.stylefent.guns.entity.query;

import lombok.Data;

/**
 * 礼物
 *
 * @author leiyu
 */
@Data
public class GiftQuery extends BaseQuery {
    private Long giftId;
    private Long linkId;
    /**
     * 礼物数量
     */
    private Integer count;

    /**
     * 直播ID
     */
    private Long liveId;
}
