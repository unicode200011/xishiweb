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
 * 举报列表
 * </p>
 *
 * @author stylefeng
 * @since 2019-12-17
 */
@Data
@TableName("eb_complain")
public class Complain extends Model<Complain> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 举报用户id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 被举报用户id
     */
    @TableField("link_uid")
    private Long linkUid;
    /**
     * 举报理由
     */
    private String reason;
    /**
     * 举报者用户名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 被举报者用户名
     */
    @TableField("link_uname")
    private String linkUname;
    /**
     * 举报时间
     */
    @TableField("create_time")
    private Date createTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
