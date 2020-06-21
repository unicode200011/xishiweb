package com.stylefent.guns.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class TimelineVo {
    private String title;
    private String time;
    /**
     * 展示类型 0 左边 1 右边
     */
    private Integer type;
    private String content;

    /**
     * 实体类型 0 动态 1 计划
     */
    private Integer voType;
    /**
     * 资源类型 0 图片 1 语音 2 视频 3 道具
     */
    private Integer contentType;

    private String resource;

    private String videoCover;

    private List<String> resourceArr;

}
