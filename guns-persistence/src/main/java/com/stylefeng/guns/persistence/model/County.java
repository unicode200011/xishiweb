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
 * 区县
 * </p>
 *
 * @author stylefeng
 * @since 2019-04-03
 */
@Data
@TableName("eb_county")
public class County extends Model<County> {

    private static final long serialVersionUID = 1L;
    @TableField(exist = false)
    public Integer county_id;
    @TableField(exist = false)
    public Integer city_id;

    /**
     * 区县ID
     */
    @TableId(value = "county_id", type = IdType.AUTO)
    private Integer countyId;
    /**
     * 区县名
     */
    private String county;
    /**
     * 城市ID
     */
    @TableField("city_id")
    private Integer cityId;
    /**
     * 邮政编码
     */
    @TableField("zip_code")
    private String zipCode;
    private String code;

    @TableField(exist = false)
    private String province;

    @TableField(exist = false)
    private String city;


    @Override
    protected Serializable pkVal() {
        return this.countyId;
    }

}
