package com.pom.socket.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @功能说明:
 * @创建者: Pom
 * @创建时间: 2020-03-18 23:51:15
 * @公司名称: 180830.com
 * @版本:V1.0
 */
public class Server {
	public static void main(String[] args) {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			ChannelFuture channelFuture = bootstrap.group(boss, worker)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast("logging",new LoggingHandler("DEBUG"));//设置log监听器，并且日志级别为debug，方便观察运行流程
				        ch.pipeline().addLast("http-codec",new HttpServerCodec());//设置解码器
				        ch.pipeline().addLast("aggregator",new HttpObjectAggregator(65536));//聚合器，使用websocket会用到
				        ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());//用于大数据的分区传输
				        ch.pipeline().addLast("handler",new WebSocketHandler());//自定义的业务handler
					}
				})
				.channel(NioServerSocketChannel.class)
				.bind(81);
			
			Debugger.startDaemon(()->{
				System.out.println();
			});
			
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
}
