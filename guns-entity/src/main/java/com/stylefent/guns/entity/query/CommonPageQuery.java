package com.stylefent.guns.entity.query;

import com.stylefent.guns.entity.query.BaseQuery;
import lombok.Data;


/**
 * @author: lx
 */
@Data
public class CommonPageQuery extends BaseQuery {
    private Integer pageSize = 20;
    private Integer pageNum = 1;
    private String keyWord;
    private Integer type;
}
