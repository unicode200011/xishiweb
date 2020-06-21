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
 * 邀请码
 * </p>
 *
 * @author lx
 * @since 2019-01-16
 */
@TableName("eb_invitation_code")
@Data
public class InvitationCode extends Model<InvitationCode> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 邀请码
     */
    private String code;
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 已邀请人数
     */
    @TableField("user_num")
    private Integer userNum;
    @TableField("create_time")
    private Date createTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
