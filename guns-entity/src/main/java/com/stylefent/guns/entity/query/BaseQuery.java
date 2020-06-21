package com.stylefent.guns.entity.query;


import lombok.Data;

@Data
public class BaseQuery {
    public int page = 1;
    public int rows = 5;
    public Long userId;
    public Integer opType = 0;
}
