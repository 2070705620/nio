package com.pom.socket.n0;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @功能说明:
 * @创建者: Pom
 * @创建时间: 2020-03-10 17:00:44
 * @公司名称: 180830.com
 * @版本:V1.0
 */
public class Client0 {
    /**
     * Netty创建全部都是实现自AbstractBootstrap。
     * 客户端的是Bootstrap，服务端的则是    ServerBootstrap。
     **/
    public static void main(String[] args) {
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
        	Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker);
            bootstrap.channel(NioSocketChannel.class); //创建nio类型的channel
            bootstrap.handler(new NettyClientFilter());
            // 连接服务端
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8013).sync();
            
            Debugger.startAndJoin(()->{
            	//连接
            	Channel channel = channelFuture.channel(); //无论执行多少次都是同一个
            	System.out.println();
            	//断开连接
            	//channel.close()
            	
            	channel.writeAndFlush("abcd");
            	//发送 消息ch.writeAndFlush(str+ "\r\n");
            	
            });
        }catch (InterruptedException e) {
        	e.printStackTrace();
		}finally {
			worker.shutdownGracefully();
		}
    }
}
