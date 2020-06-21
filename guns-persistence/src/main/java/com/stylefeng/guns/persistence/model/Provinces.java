package com.stylefeng.guns.persistence.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 省份
 * </p>
 *
 * @author stylefeng
 * @since 2019-09-17
 */
@Data
@TableName("eb_provinces")
public class Provinces extends Model<Provinces> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "province_id", type = IdType.AUTO)
    private Integer provinceId;
    private String province;
    @TableField("country_id")
    private Integer countryId;
    @TableField("created_time")
    private Integer createdTime;
    private String code;


    @Override
    protected Serializable pkVal() {
        return this.provinceId;
    }

}
