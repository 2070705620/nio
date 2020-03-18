package com.pom.socket.n0;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * @功能说明:
 * @创建者: Pom
 * @创建时间: 2020-03-10 16:04:21
 * @公司名称: 180830.com
 * @版本:V1.0
 */
public class MultiServer {
	final static ByteBuf buffer = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi\r\n", Charset.forName("UTF-8")));
	
	public static List<NioEventLoop> eloops = new ArrayList<>();
	static {
		Debugger.set("server", true);
		Debugger.set("eloops", eloops);
	}
	
	public static void main(String[] args) {
		
		
		EventLoopGroup  boss = new NioEventLoopGroup(2, new DefaultThreadFactory("nboss", true));
		
		for(int i=0; i < 1; i++) {
			EventLoopGroup  worker = new NioEventLoopGroup(12, new DefaultThreadFactory("nworker", true));
			ServerBootstrap bootstrap = new ServerBootstrap();
			Debugger.set("bootstrap"+i, bootstrap);
			bootstrap.group(boss, worker)
			.channel(NioServerSocketChannel.class)
			.childHandler(new ChannelInitializer<SocketChannel>() {
				protected void initChannel(SocketChannel  ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();
					
					pipeline.addLast("encoder",new StringEncoder());
					pipeline.addLast("decoder",new StringDecoder());
	                pipeline.addLast("3",new InboundB());
				};
			});
			int port = 8013 + i;
			new Thread(()->{
				try {
					ChannelFuture f = bootstrap.bind(port).sync();
					f.channel().closeFuture().sync();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
		}
		Debugger.startDaemon(()->{
			System.out.println();
		});
	}
}
