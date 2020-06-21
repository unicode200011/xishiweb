package com.stylefeng.guns.rest.modular.netty_socketio.entity;

import lombok.Data;

@Data
public class PushMessage {
    private String loginUserNum;

    private String content;
}
