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
 * 代理商处理记录
 * </p>
 *
 * @author stylefeng
 * @since 2019-11-26
 */
@Data
@TableName("eb_agent_deal_record")
public class AgentDealRecord extends Model<AgentDealRecord> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 代理商id
     */
    @TableField("agent_id")
    private Long agentId;
    /**
     * 代理商id
     */
    @TableField("admin_name")
    private String adminName;
    /**
     * 处理人
     */
    @TableField("admin_id")
    private Integer adminId;
    /**
     * 处理备注
     */
    private String remark;
    /**
     * 申请时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 处理时间
     */
    @TableField("update_time")
    private Date updateTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
