package com.stylefent.guns.entity.message;

import lombok.Data;

@Data
public class RpcResponse {
    private String id;
    private Object data;
    // 0=success -1=fail
    private int status;
}
