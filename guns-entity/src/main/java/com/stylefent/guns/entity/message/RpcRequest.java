package com.stylefent.guns.entity.message;

import lombok.Data;

@Data
public class RpcRequest {
    private String id;
    private Object data;
}
