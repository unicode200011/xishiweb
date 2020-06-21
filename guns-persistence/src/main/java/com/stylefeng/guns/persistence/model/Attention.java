package com.stylefeng.guns.persistence.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 关注
 * </p>
 *
 * @author lx
 * @since 2018-06-27
 */
@TableName("eb_attention")
@Data
public class Attention extends Model<Attention> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 0 : 关注  1：拉黑
     */
    private Integer type;
    /**
     * 被关注人ID
     */
    @TableField("link_id")
    private Long linkId;
    /**
     * 关注时间
     */
    @TableField("create_time")
    private Date createTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
