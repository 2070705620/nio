package com.pom.socket.n3;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

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
        	
        	Debugger.set(123, bootstrap);
        	
            bootstrap.group(worker);
            bootstrap.channel(NioSocketChannel.class); //创建nio类型的channel
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            	@Override
            	protected void initChannel(SocketChannel ch) throws Exception {
            		ChannelPipeline pip = ch.pipeline();
            		pip.addLast(new MessageToByteEncoder<byte[]>() {
            			@Override
            			protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf out) throws Exception {
            				System.out.println("发送:" + Arrays.toString(msg));
            				out.writeBytes(msg);
            			}
					});
            		/*pip.addLast(new MessageToByteEncoder<String>() {
            			@Override
            			protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
            				out.writeShort(MessageConfig.MAGIC);
            				byte[]body = msg.getBytes("UTF-8");
            				out.writeShort(body.length);
            				out.writeBytes(body);
            			}
					});*/
            		pip.addLast(new StringDecoder());
            	}
			});
            // 连接服务端
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8013).sync();
            
            Debugger.startAndJoin(()->{
            	//连接
            	Channel channel = channelFuture.channel(); //无论执行多少次都是同一个
            	System.out.println();
            	//断开连接
            	//channel.close()
            	
            	//channel.writeAndFlush("abcd");
            	//发送 消息ch.writeAndFlush(str+ "\r\n");
            	
            });
        }catch (InterruptedException e) {
        	e.printStackTrace();
		}finally {
			worker.shutdownGracefully();
		}
    }
}
