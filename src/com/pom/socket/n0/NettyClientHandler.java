package com.pom.socket.n0;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @功能说明:
 * @创建者: Pom
 * @创建时间: 2020-03-10 17:04:19
 * @公司名称: 180830.com
 * @版本:V1.0
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<String> {

	 @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("response: " + msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("正在连接... ");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接关闭! ");
        super.channelInactive(ctx);
    }

}
