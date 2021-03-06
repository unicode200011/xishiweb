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
 * 代理商钱包记录
 * </p>
 *
 * @author stylefeng
 * @since 2019-12-30
 */
@Data
@TableName("eb_agent_wallet_record")
public class AgentWalletRecord extends Model<AgentWalletRecord> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 代理商id
     */
    @TableField("agent_id")
    private Long agentId;
    /**
     * 关联主播id
     */
    @TableField("shower_id")
    private Long showerId;
    /**
     * 收益类型 2.获得礼物+ 3：观看常规收费直播 4观看计时收费直播 5:看电影 6：坐骑 7：靓号8：提现
     */
    private Integer type;
    /**
     * 收支类型：0 ：收入 1：支出
     */
    private Integer io;
    /**
     * 备注
     */
    private String remark;
    /**
     * 收支对象 用户id
     */
    @TableField("link_uid")
    private Long linkUid;
    /**
     * 插入记录时代理商钱包余额
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
    /**
     * 直播记录id
     */
    @TableField("live_record_id")
    private Long liveRecordId;

    @TableField("create_time")
    private Date createTime;

    private BigDecimal amount;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
