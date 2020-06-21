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
 * 公共数据
 * </p>
 *
 * @author stylefeng
 * @since 2019-07-25
 */
@Data
@TableName("eb_common_data")
public class CommonData extends Model<CommonData> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * key
     */
    @TableField("data_key")
    private String dataKey;
    /**
     * 类型
     */
    private String type;
    /**
     * 值 通过 key 获取唯一数据
     */
    private String value;
    /**
     * 值 描述
     */
    private String description;
    /**
     * 拓展参数
     */
    private String extra;
    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    /**
     * 文本类型 0 ：富文本 1 ：文本域 2 纯数字 3：可带两位小数数字
     */
    @TableField("content_type")
    private Integer contentType;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
