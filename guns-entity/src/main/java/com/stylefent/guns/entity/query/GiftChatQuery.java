package com.stylefent.guns.entity.query;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class GiftChatQuery extends BaseQuery {

    /**
     * 发送消息的用户的id
     */
    private Long senderId;

    /**
     * 接收消息的用户的id
     */
    private Long receiverId;

    /**
     * 是否为礼物聊天的发起人
     * 0：不是，1：是
     * 在首页扇形图标内进入礼物聊天页面时必传，且固定传1，表示此用户主动发起了礼物聊天
     * 在消息页面中的礼物聊天列表中进入礼物聊天页面不用传值
     */
    private Integer isInitiator;

    /**
     * 聊天内容
     */
    private String chatContent;

    /**
     * 聊天类型，0私信，1礼物聊天，3礼包
     */
    private Integer chatType;

    /**
     * 礼包送出的阳光值
     */
    private BigDecimal giftSun;
}
