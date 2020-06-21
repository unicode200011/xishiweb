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
 * 礼物
 * </p>
 *
 * @author stylefeng
 * @since 2019-11-20
 */
@Data
@TableName("eb_gift")
public class Gift extends Model<Gift> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 礼物名称
     */
    private String name;
    /**
     * 礼物单价（西施币）
     */
    private BigDecimal money;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 礼物样式
     */
    private String image;
    /**
     * 类型 0:小礼物 1:大礼物
     */
    private Integer type;
    /**
     * 礼物效果: 0.无; 1.666[上浮,抖动,渐隐]; 2.fans[上浮,抖动,螺旋飞出]; 3.打Call[上浮,放大,渐隐]; 4.飞机[从右到左弧线移动]; 5.挠痒痒[上浮,手指头移动,消失]; 6.跑车[从右到左直线移动]; 7.热气球[从屏幕下方上升至屏幕上方]; 8.星星[闪动进入,抖动,闪动退出]; 9.荧光棒[上浮,晃动,渐隐]; 10.游艇[从右到左直线移动]
     */
    private Integer animation;
    /**
     * 排序值 大靠前
     */
    private Integer num;
    /**
     * 状态：0：显示  1：隐藏
     */
    private Integer state;
    /**
     * 使用数量
     */
    @TableField("use_count")
    private Long useCount;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
