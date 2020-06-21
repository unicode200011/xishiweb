package com.stylefent.guns.entity.dto;

import lombok.Data;

@Data
public class MovieResp {
    /**
     * 电影id
     */
    private Long id;
    /**
     * 电影名
     */
    private String name;
    /**
     * 电影封面
     */
    private String cover;
    /**
     * 电影简介
     */
    private String remark;
    /**
     * 电影观看人数
     */
    private Integer playNum;
}
