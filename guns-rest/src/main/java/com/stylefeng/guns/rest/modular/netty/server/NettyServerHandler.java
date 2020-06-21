package com.stylefeng.guns.rest.modular.netty.server;

import com.stylefeng.guns.rest.modular.netty.server.service.NettyServerService;
import com.stylefent.guns.entity.message.RpcRequest;
import com.stylefent.guns.entity.message.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    NettyServerService nettyServerService;

    /**
     * 客户端发消息会触发
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        log.info("接收到客户端发送消息");

        RpcRequest request = (RpcRequest) msg;
        log.info("接收到客户端信息:" + request.toString());
        //返回的数据结构
        RpcResponse response = new RpcResponse();
        response.setId(UUID.randomUUID().toString());
        response.setData("server响应结果:收到了你的消息");
        response.setStatus(1);
        ctx.writeAndFlush(response);
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        /**flush：将消息发送队列中的消息写入到 SocketChannel 中发送给对方，为了频繁的唤醒 Selector 进行消息发送
         * Netty 的 write 方法并不直接将消息写如 SocketChannel 中，调用 write 只是把待发送的消息放到发送缓存数组中，再通过调用 flush
         * 方法，将发送缓冲区的消息全部写入到 SocketChannel 中
         * */
        System.out.println("服务端接收数据完毕..");
        ctx.flush();
    }
    /**
     * 客户端去和服务端连接成功时触发
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        RpcResponse response = new RpcResponse();
        response.setId(UUID.randomUUID().toString());
        response.setData("welcome you connect succeed");
        response.setStatus(0);
        ctx.writeAndFlush(response);
        log.info("客户端{}连接成功",ctx.channel().remoteAddress().toString());
    }

    /**
     * 发生异常触发
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
