package com.stylefeng.guns.persistence.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 邀请用户列表
 * </p>
 *
 * @author lx
 * @since 2019-01-16
 */
@TableName("eb_invitation_user")
@Data
public class InvitationUser extends Model<InvitationUser> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户ID
     */
    @TableField("from_uid")
    private Long fromUid;
    /**
     * 被邀请用户
     */
    @TableField("to_uid")
    private Long toUid;
    /**
     * 使用的邀请码
     */
    private String code;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String account;

    @TableField(exist = false)
    private String rlbNum;

    @TableField(exist = false)
    private BigDecimal boyMoney;

    @TableField(exist = false)
    private BigDecimal girlMoney;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
