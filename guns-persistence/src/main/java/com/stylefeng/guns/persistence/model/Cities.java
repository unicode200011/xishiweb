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
 * 城市
 * </p>
 *
 * @author stylefeng
 * @since 2019-09-17
 */
@Data
@TableName("eb_cities")
public class Cities extends Model<Cities> {

    private static final long serialVersionUID = 1L;

    /**
     * 城市ID
     */
    @TableId(value = "city_id", type = IdType.AUTO)
    private Integer cityId;
    /**
     * 城市名称
     */
    private String city;
    /**
     * 省份名称
     */
    @TableField("province_id")
    private Integer provinceId;
    /**
     * 邮政编码
     */
    @TableField("zip_code")
    private String zipCode;
    /**
     * 城市代码
     */
    private String code;


    @Override
    protected Serializable pkVal() {
        return this.cityId;
    }

}
