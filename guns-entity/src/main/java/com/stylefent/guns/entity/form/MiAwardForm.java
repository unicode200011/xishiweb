package com.stylefent.guns.entity.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 米赏
 *
 * @author: LEIYU
 */
@Data
public class MiAwardForm {

    /**
     * 米赏ID
     */
    private Long msId;
    /**
     * 标题
     */
    private String title;
    /**
     * 米赏说明
     */
    private String content;
    /**
     * 分类ID
     */
    private String[] typeArr;
    /**
     * 发布人ID
     */
    private Long userId;
    /**
     * 应赏人数
     */
    private Integer userNum;
    /**
     * 每人获得金额
     */
    private BigDecimal userMoney;
    /**
     * 可见 0:公开 1:私密
     */
    private Integer permType = 0;
    /**
     * 范围 0:全国 1:同城
     */
    private Integer rangeType = 0;
    /**
     * 经度
     */
    private BigDecimal lgt;
    /**
     * 纬度
     */
    private BigDecimal lat;
    /**
     * 城市
     */
    private String city;
    /**
     * 省份
     */
    private String province;
    /**
     * 截止日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
}
