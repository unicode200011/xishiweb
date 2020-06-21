package com.stylefent.guns.entity.query;

import lombok.Data;

@Data
public class MusicQuery extends BaseQuery {
    private Long musicId;
    private String name;
    private Long typeId;
}
