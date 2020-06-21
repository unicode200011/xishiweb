package com.stylefeng.guns.rest.modular.netty.server.service;

import com.stylefent.guns.entity.message.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NettyServerServiceImpl implements NettyServerService {

    private static Map<String, ChannelHandlerContext> clientMap = new ConcurrentHashMap<>();
    @Override
    public void addChannel(ChannelHandlerContext ctx,String clientName) {
        clientMap.put(clientName,ctx);
    }

    @Override
    public void removeChannel(ChannelHandlerContext ctx,String clientName) {
        clientMap.remove(clientName);
    }

    @Override
    public void sendMessageToOne(String clientName,String message) {
        ChannelHandlerContext ctx = clientMap.get(clientName);
        RpcResponse response = new RpcResponse();
        response.setId(UUID.randomUUID().toString());
        response.setData(message);
        response.setStatus(0);
        ctx.writeAndFlush(response);
    }

    @Override
    public ChannelHandlerContext getCtxByName(String clientName) {
        return clientMap.get(clientName);
    }

    public void sendMesgToAll(String message){
        RpcResponse response = new RpcResponse();
        response.setId(UUID.randomUUID().toString());
        response.setData(message);
        response.setStatus(0);
    }

}
