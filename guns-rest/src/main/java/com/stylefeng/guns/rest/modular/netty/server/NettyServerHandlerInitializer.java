package com.stylefeng.guns.rest.modular.netty.server;

import com.stylefeng.guns.rest.modular.netty.server.service.NettyServerService;
import com.stylefent.guns.entity.message.RpcDecoder;
import com.stylefent.guns.entity.message.RpcEncoder;
import com.stylefent.guns.entity.message.RpcRequest;
import com.stylefent.guns.entity.message.RpcResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import org.springframework.beans.factory.annotation.Autowired;

public class NettyServerHandlerInitializer extends ChannelInitializer<Channel> {



    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline()
                .addLast(new RpcDecoder(RpcRequest.class)) //解码request
                .addLast(new RpcEncoder(RpcResponse.class)) //编码response
                .addLast(new NettyServerHandler());
    }
}
