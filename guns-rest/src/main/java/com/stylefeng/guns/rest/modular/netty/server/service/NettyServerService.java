package com.stylefeng.guns.rest.modular.netty.server.service;

import io.netty.channel.ChannelHandlerContext;

public interface NettyServerService {
     void addChannel(ChannelHandlerContext ctx,String clientName);
     void removeChannel(ChannelHandlerContext ctx,String clientName);
     void sendMessageToOne(String clientName,String message);
    ChannelHandlerContext getCtxByName(String clientName);
}
