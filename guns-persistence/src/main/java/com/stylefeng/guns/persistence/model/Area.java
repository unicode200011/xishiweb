package com.stylefeng.guns.persistence.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 城市
 * </p>
 *
 * @author stylefeng
 * @since 2019-07-23
 */
@Data
@TableName("eb_area")
public class Area extends Model<Area> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 城市名/省份名
     */
    private String name;
    /**
     * 区域类型 0:省  1:市  2:区县  3:镇
     */
    private Integer type;
    /**
     * 父级ID
     */
    @TableField("parent_id")
    private Long parentId;
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
