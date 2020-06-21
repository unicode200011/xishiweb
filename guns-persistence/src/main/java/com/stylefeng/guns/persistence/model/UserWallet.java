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
 * 用户账户
 * </p>
 *
 * @author stylefeng
 * @since 2019-12-06
 */
@Data
@TableName("eb_user_wallet")
public class UserWallet extends Model<UserWallet> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 西施币数量
     */
    @TableField("gb_moeny")
    private BigDecimal gbMoeny;
    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;
    private Long version;
    /**
     * 合计收到打赏（西施币）
     */
    @TableField("gift_amount")
    private BigDecimal giftAmount;
    /**
     * 消费总值
     */
    @TableField("give_amount")
    private BigDecimal giveAmount;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
