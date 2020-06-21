package com.stylefent.guns.entity.query;

import lombok.Data;

import java.io.Serializable;

/**
 * @Project: &nbspguns
 * @Package: com.stylefent.guns.entity.query
 * @Author: 无痕无边
 * @CreateDate: 2019/04/10 16:58
 * @Version: 1.0
 * @Description:
 * @Company:
 */
@Data
public class SearchQuery implements Serializable {

    private String keyWords;//关键字
    private Integer userId;//用户id
    private Integer id;
//    private Long linkId;//编号

    private Integer page;
    private Integer rows;
    //0年检 1自检 2限速器
    private Integer type;

}
