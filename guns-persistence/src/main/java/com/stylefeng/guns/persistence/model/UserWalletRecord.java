package com.stylefeng.guns.persistence.model;

import com.baomidou.mybatisplus.activerecord.Model;
import cn.afterturn.easypoi.excel.annotation.Excel;
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
 * 用户钱包记录
 * </p>
 *
 * @author stylefeng
 * @since 2019-12-06
 */
@Data
@TableName("eb_user_wallet_record")
public class UserWalletRecord extends Model<UserWalletRecord> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("user_id")
    private Long userId;
    /**
     * 消费总金额
     */
    private BigDecimal amount;
    /**
     * 收支方式:0-收入 1-支出
     */
    @TableField("use_type")
    private Integer useType;
    /**
     * 消费类型:  0.充值+  1.送礼物-  2.获得礼物+ 3：观看常规收费直播 4观看计时收费直播 5:看电影 6：坐骑 7：靓号
     */
    private Integer type;
    /**
     * 备注
     */
    private String remark;


    @TableField("create_time")
    private Date createTime;
    /**
     * 收支对象 用户id
     */
    @TableField("link_uid")
    private Long linkUid;
    /**
     * 插入记录时用户钱包余额
     */
    @TableField("wallet_amount")
    private BigDecimal walletAmount;
    /**
     * 送礼物时 保存的礼物id  直播时的 直播间id
     */
    @TableField("cust_id")
    private Long custId;
    /**
     * 消费数量 礼物个数 计时模式 分钟数
     */
    @TableField("cust_num")
    private Integer custNum;
    /**
     * 消费的对象名字 如礼物名  直播间名字
     */
    @TableField("cust_name")
    private String custName;



    @TableField(exist = false)
    private String xishiNum;

    @TableField(exist = false)
    private String xishiNum2;


    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String linkUName;

    @TableField(exist = false)
    private String agent;

    @TableField(exist = false)
    private String typeStr;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
