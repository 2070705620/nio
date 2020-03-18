package com.pom.socket.n0;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @功能说明:
 * @创建者: Pom
 * @创建时间: 2020-03-12 22:10:45
 * @公司名称: 180830.com
 * @版本:V1.0
 */
public class MultiClient {
	public static Channel[] channels = new Channel[100];
	public static Bootstrap bootstrap = null;
	public static void main(String[] args) {
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			bootstrap = new Bootstrap();
			bootstrap.channel(NioSocketChannel.class)
				.handler(new NettyClientFilter())
				.group(worker);
			for(int i=0; i < channels.length; i++) {
				/*channels[i] = */bootstrap.connect("127.0.0.1", 8013).channel();
			}
//			for(int i=0; i < channels.length; i++) {
//				/*channels[i] = */bootstrap.connect("127.0.0.1", 8014).channel();
//			}
			Debugger.startAndJoin(()->{
				System.out.println();
			});
		}finally {
			worker.shutdownGracefully();
		}
	}
}
