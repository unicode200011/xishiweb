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
 * 电影
 * </p>
 *
 * @author stylefeng
 * @since 2019-11-12
 */
@Data
@TableName("eb_movie")
public class Movie extends Model<Movie> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 上传渠道 0: 平台 1:代理商
     */
    private Integer source;
    /**
     * 封面图片
     */
    private String cover;
    /**
     * 上架时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 播放量
     */
    @TableField("play_num")
    private Long playNum;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 电影链接
     */
    private String url;
    /**
     * 电影分类
     */
    @TableField("cate_id")
    private Integer cateId;

    @TableField(exist = false)
    private String cateName;
    /**
     * 状态 0：上架  1：下架 2：删除
     */
    private Integer state;
    /**
     * 上传人
     */
    @TableField("upload_user")
    private String uploadUser;

    @TableField("watch_num")
    private Long watchNum;
    /**
     * 电影简介
     */
    private String remark;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
