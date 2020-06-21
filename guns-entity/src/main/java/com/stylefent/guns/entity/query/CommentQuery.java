package com.stylefent.guns.entity.query;

import lombok.Data;

/**
 * @author: LEIYU
 */
@Data
public class CommentQuery extends BaseQuery {
    /**
     * 短视频ID
     */
    private Long videoId;
    /**
     * 评论ID
     */
    private Long commentId;
    /**
     * 回复内容
     */
    private String content;
    /**
     * 回复对象
     */
    private Long tUid;
    /**
     * 回复ID
     */
    private Long commentRid;
    /**
     * 拓展字段
     */
    private String extra;
}
