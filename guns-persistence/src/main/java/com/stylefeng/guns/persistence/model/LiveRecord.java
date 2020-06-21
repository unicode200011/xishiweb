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
 * @since 2019-12-24
 */
@Data
@TableName("eb_live_record")
public class LiveRecord extends Model<LiveRecord> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 直播id
     */
    @TableField("live_id")
    private Long liveId;
    /**
     * 直播封面
     */
    private String cover;
    /**
     * 直播标题
     */
    private String name;
    /**
     * 当前观看人数
     */
    @TableField("live_watch_now")
    private Long liveWatchNow;
    /**
     * 本场总管看人数
     */
    @TableField("live_watch_total")
    private Long liveWatchTotal;
    /**
     * 主播名字
     */
    @TableField("shower_name")
    private String showerName;
    /**
     * 已开播时长 分钟
     */
    @TableField("show_time")
    private Long showTime;
    /**
     * 本场直播当前总贡献量
     */
    private BigDecimal amount;
    /**
     * 直播状态 0：正在直播 1：直播结束
     */
    private Integer state;
    /**
     * 直播模式 0：免费 1：密码模式 2：常规收费  3：计时收费
     */
    @TableField("live_mode")
    private Integer liveMode;
    /**
     * 直播收费价格 收费模式有效  
     */
    @TableField("live_price")
    private BigDecimal livePrice;
    /**
     * 开始时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 结束时间
     */
    @TableField("end_time")
    private Date endTime;
    /**
     * 密码  密码模式有效
     */
    @TableField("live_pwd")
    private String livePwd;
    /**
     * 用户id 
     */
    @TableField("user_id")
    private Long userId;

    @TableField(exist = false)
    private String xishiNum;

    @TableField(exist = false)
    private Integer liveGiftNum;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
