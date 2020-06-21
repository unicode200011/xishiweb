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
 * 直播等级
 * </p>
 *
 * @author stylefeng
 * @since 2019-11-20
 */
@Data
@TableName("eb_live_grade")
public class LiveGrade extends Model<LiveGrade> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 会员等级
     */
    private Integer level;
    /**
     * 等级标识
     */
    private String image;
    /**
     * 达标接收西施币
     */
    private BigDecimal amount;
    /**
     * 等级颜色
     */
    private String color;
    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;
    /**
     * 等级名字
     */
    @TableField("level_name")
    private String levelName;
    /**
     * 颜色rgb
     */
    private String rgb;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
