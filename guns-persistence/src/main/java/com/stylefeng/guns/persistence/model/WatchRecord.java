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
 * @since 2020-02-25
 */
@TableName("eb_watch_record")
@Data
public class WatchRecord extends Model<WatchRecord> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("user_id")
    private Long userId;
    @TableField("live_record_id")
    private Integer liveRecordId;
    @TableField("create_time")
    private Date createTime;
    /**
     * 退出时间
     */
    @TableField("end_time")
    private Date endTime;
    /**
     * 是否退出直播间 0：否 1：是
     */
    private Integer state;
    private Integer time;

    @TableField(exist = false)
    private String showerName;

    @TableField(exist = false)
    private String roomNum;

    @TableField(exist = false)
    private String userXishiNum;

    @TableField(exist = false)
    private String showerXishiNum;

    @TableField(exist = false)
    private BigDecimal amount = BigDecimal.ZERO;

    @TableField(exist = false)
    private Integer giftNum = 0;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
