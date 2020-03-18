package com.pom.socket.n1;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;

import com.pom.socket.n0.InboundB;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * @功能说明:
 * @创建者: Pom
 * @创建时间: 2020-03-13 20:49:33
 * @公司名称: 180830.com
 * @版本:V1.0
 */
public class ServerHeartBeat {
	public static void main(String[] args) {
ServerBootstrap bootstrap = new ServerBootstrap();
		
		EventLoopGroup  boss = new NioEventLoopGroup(1, new DefaultThreadFactory("NettyServerBoss", true));
		EventLoopGroup  worker = new NioEventLoopGroup(12, new DefaultThreadFactory("NettyServerWorker", true));
		Debugger.set("B", boss);
		Debugger.set("W", worker);
		
		Debugger.startDaemon(()->{
			System.out.println();
		});
		try {
			bootstrap.group(boss, worker)
			.channel(NioServerSocketChannel.class)
			.localAddress(8013)
			.childHandler(new ChannelInitializer<SocketChannel>() {
				private Set<ChannelHandlerContext> ctxs = new HashSet<>();
				protected void initChannel(SocketChannel  ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();
					
					//定时器处理类
					pipeline.addLast(new IdleStateHandler(30, 30, 60));
					
					pipeline.addLast("encoder",new StringEncoder());
					pipeline.addLast("decoder",new StringDecoder());
					pipeline.addLast(new SimpleChannelInboundHandler<String>() {
						@Override
						protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
							System.out.println("接收到的消息: " + msg);
							ctx.channel().writeAndFlush(msg + RandomStringUtils.randomNumeric(10)+"\r\n");
						}
						@Override
						public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
							if(evt instanceof IdleStateEvent) {
								IdleStateEvent event = (IdleStateEvent)evt;
								if(event.state() == IdleState.ALL_IDLE) {
									ChannelFuture closeAction = ctx.channel().writeAndFlush("eureka-client准备下线，因为没有收到心跳");
									closeAction.addListener(new ChannelFutureListener() {
										@Override
										public void operationComplete(ChannelFuture future) throws Exception {
											future.channel().close();
										}
									});
								}
							}else {
								super.userEventTriggered(ctx, evt);
							}
						}
					});
				};
			});
			ChannelFuture f = bootstrap.bind().sync();
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
            try {
            	
				boss.shutdownGracefully().sync();
				worker.shutdownGracefully().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
           
        }
	}
}
