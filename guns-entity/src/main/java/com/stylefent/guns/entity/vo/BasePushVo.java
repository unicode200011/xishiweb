package com.stylefent.guns.entity.vo;

import lombok.Data;

/**
 * 推送父类
 *
 * @author: LEIYU
 */
@Data
public class BasePushVo {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 关联ID
     */
    private Long linkId;

    /**
     * 消息ID
     */
    private Long msgId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 封面
     */
    private String cover;

    /**
     * 消息类型 0 点赞消息 1 评论消息 2 关注消息 3 看豆交易 4 系统消息
     */
    private Integer msgType;
}
