package com.stylefeng.guns.persistence.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author stylefeng
 * @since 2019-12-30
 */
@Data
@TableName("eb_agent_invite")
public class AgentInvite extends Model<AgentInvite> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 被邀请用户id
     */
    @TableField("user_id")
    private Long userId;


    /**
     * 邀请时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 家族编号
     */
    @TableField("agent_id")
    private Long agentId;

    @TableField("agent_num")
    private String agentNum;
    /**
     * 是否同意：0 未处理 1 已同意 2：已拒绝
     */
    @TableField("join_state")
    private Integer joinState;
    /**
     * 类型 0：用户申请  1：打理人邀请
     */
    private Integer type;
    /**
     * 状态：0：正常 1：已过期
     */
    private Integer state;

    @TableField(exist = false)
    private String name;

    @TableField(exist = false)
    private String xishiNum;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
