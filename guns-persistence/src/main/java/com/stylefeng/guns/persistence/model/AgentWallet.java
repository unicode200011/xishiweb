package com.stylefeng.guns.persistence.model;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
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
@TableName("eb_agent_wallet")
@Data
public class AgentWallet extends Model<AgentWallet> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 账户余额
     */
    @TableField("gb_amount")
    private BigDecimal gbAmount;
    /**
     * 收益总额
     */
    @TableField("total_gift_amount")
    private BigDecimal totalGiftAmount;
    /**
     * 代理商id
     */
    @TableField("agent_id")
    private Long agentId;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
