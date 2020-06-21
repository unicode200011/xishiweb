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
 * 意见反馈类型
 * </p>
 *
 * @author stylefeng
 * @since 2019-12-17
 */
@Data
@TableName("eb_suggestion")
public class Suggestion extends Model<Suggestion> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 反馈用户ID
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 类型ID
     */
    @TableField("type_id")
    private Long typeId;
    /**
     * 管理员ID
     */
    @TableField("admin_id")
    private Integer adminId;
    /**
     * 反馈内容
     */
    private String content;
    /**
     * 状态 0 待处理 1 已处理 2 删除
     */
    private Integer state;
    /**
     * 回复内容1
     */
    @TableField("first_rep_content")
    private String firstRepContent;
    /**
     * 回复内容2
     */
    @TableField("second_rep_content")
    private String secondRepContent;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;

    @TableField(exist = false)
    private String userName;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
