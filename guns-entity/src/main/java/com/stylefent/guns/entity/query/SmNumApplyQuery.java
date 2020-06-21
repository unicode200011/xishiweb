package com.stylefent.guns.entity.query;

import lombok.Data;

@Data
public class SmNumApplyQuery extends BaseQuery {

    /**
     * 获取靓号详情用的参数，填入靓号值
     */
    private String num;

    /**
     * 靓号申请接口用的参数，表示原靓号
     */
    private String oldNum;

    /**
     * 靓号申请接口用的参数，表示新靓号
     */
    private String newNum;

    /**
     * 礼包购买记录的id
     */
    private Long giftShoppingRecordId;

}
