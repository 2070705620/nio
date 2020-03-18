package com.pom.socket.n3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @功能说明:
 * @创建者: Pom
 * @创建时间: 2020-03-15 19:14:39
 * @公司名称: 180830.com
 * @版本:V1.0
 */
public class InboundHandler2 extends ChannelInboundHandlerAdapter{
	
	private static final Logger logger = LoggerFactory.getLogger(InboundHandler2.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof ByteBuf) {
			byte[]buffer = new byte[((ByteBuf)msg).readableBytes()];
			((ByteBuf)msg).readBytes(buffer);
			System.out.println(new String(buffer));
		}
		logger.debug("msg: {}", msg);
		super.channelRead(ctx, msg);
	}

}
