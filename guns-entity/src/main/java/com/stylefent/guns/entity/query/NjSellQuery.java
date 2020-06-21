package com.stylefent.guns.entity.query;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 买卖牛角
 *
 * @author: LEIYU
 */
@Data
public class NjSellQuery extends BaseQuery {
    /**
     * 牛角商品ID
     */
    private Long goodsId;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 数量
     */
    private int amount;
}
