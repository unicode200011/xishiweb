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
 * 广告
 * </p>
 *
 * @author stylefeng
 * @since 2019-10-14
 */
@Data
@TableName("eb_ad")
public class Ad extends Model<Ad> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 广告标题
     */
    private String title;
    /**
     */
    private String location;
    /**
     * 广告封面
     */
    private String cover;
    /**
     * 简要说明
     */
    private String intro;
    /**
     * 备注
     */
    private String remark;
    /**
     * 广告连接地址
     */
    private String url;
    /**
     * 状态：0：正常 1：禁用
     */
    private Integer state;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
