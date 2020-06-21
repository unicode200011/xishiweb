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
 * 提现
 * </p>
 *
 * @author stylefeng
 * @since 2019-12-06
 */
@Data
@TableName("eb_withdrawal")
public class Withdrawal extends Model<Withdrawal> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("user_id")
    private Long userId;
    /**
     * 真实姓名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 提现账户/支付宝这里是支付宝账户,银行卡这里是银行卡iD
     */
    private String account;
    /**
     * 对应金额
     */
    private BigDecimal money;
    /**
     * 手续费率
     */
    private BigDecimal rate;
    /**
     * 手续费
     */
    @TableField("rate_money")
    private BigDecimal rateMoney;
    /**
     * 实际到账金额
     */
    @TableField("real_money")
    private BigDecimal realMoney;
    /**
     * 状态 0: 待审核 1:通过 2:进行中 3:驳回
     */
    private Integer state;

    /**
     * 审核人ID
     */
    @TableField("admin_id")
    private Long adminId;
    /**
     * 审核时间
     */
    @TableField("audit_time")
    private Date auditTime;
    /**
     * 审核备注
     */
    private String remark;
    /**
     * 提现单号
     */
    @TableField("order_num")
    private String orderNum;

    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;

    @TableField("pay_time")
    private Date payTime;
    @TableField("agent_id")
    private Long agentId;
    private Integer type;

    @TableField(exist = false)
    private String agentNum;

    @TableField(exist = false)
    private String xishiNum;

    @TableField(exist = false)
    private String realName;
    @TableField(exist = false)
    private String accountName;
    @TableField(exist = false)
    private String accountNum;
    @TableField(exist = false)
    private String qrcode;
    @TableField(exist = false)
    private Integer auditState;
    @TableField(exist = false)
    private Integer stateTow;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
