package com.pom.socket.n0;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @功能说明:
 * @创建者: Pom
 * @创建时间: 2020-03-12 10:34:39
 * @公司名称: 180830.com
 * @版本:V1.0
 */
public class InboundB extends ChannelInboundHandlerAdapter{
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("InboundB read" + msg.getClass() + " " + msg);
        super.channelRead(ctx, msg);
        // 从pipeline的尾巴开始找outbound
        System.out.println("W");
        System.out.println(Thread.currentThread().getName() + " deal");
        ctx.channel().writeAndFlush("what the hell is it???\r\n");
    }
}
