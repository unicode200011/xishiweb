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
 * 
 * </p>
 *
 * @author stylefeng
 * @since 2019-12-06
 */
@Data
@TableName("eb_charge_record")
public class ChargeRecord extends Model<ChargeRecord> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 充值用户
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 充值人民币金额
     */
    @TableField("rmb_amount")
    private BigDecimal rmbAmount;
    /**
     * 西施币数量
     */
    @TableField("xishi_amount")
    private BigDecimal xishiAmount;
    /**
     * 充值后钱包余额
     */
    @TableField("wallet_amount")
    private BigDecimal walletAmount;
    /**
     * 充值途径 0微信 1支付宝
     */
    private Integer source;
    /**
     * 充值时间
     */
    @TableField("create_time")
    private Date createTime;

    @TableField("order_num")
    private String orderNum;


    @TableField(exist = false)
    private String xishiNum;
    @TableField(exist = false)
    private String userName;
    @TableField(exist = false)
    private String phone;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
