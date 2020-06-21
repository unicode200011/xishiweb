package com.stylefent.guns.entity.form;

import lombok.Data;

/**
 * 直播表单
 *
 * @author: LEIYU
 */
@Data
public class LiveForm {

    private Long userId;

    private String category;

    private Long cateId;

    private String cover;

    private String title;
}
