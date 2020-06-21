package com.stylefent.guns.entity.query;

import lombok.Data;

/**
 * 订单
 *
 * @author: LEIYU
 */
@Data
public class OrderQuery extends BaseQuery {
    private Integer buyCount;
    private Long goodsId;
    private Long skuId;
    private Long addressId;
    private String phone;
}
