package com.pom.socket.n0;

import java.nio.charset.Charset;
import java.util.HashSet;
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
import io.netty.channel.EventLoopGroup;
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
public class Server0 {
	final static ByteBuf buffer = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi\r\n", Charset.forName("UTF-8")));
	public static void main(String[] args) {
		ServerBootstrap bootstrap = new ServerBootstrap();
		
		EventLoopGroup  boss = new NioEventLoopGroup(1, new DefaultThreadFactory("NettyServerBoss", true));
		EventLoopGroup  worker = new NioEventLoopGroup(12, new DefaultThreadFactory("NettyServerWorker", true));
		
		Debugger.set(111, bootstrap);
		Debugger.set(20, boss);
		Debugger.set(30, worker);
		
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
					
					pipeline.addLast("decoder",new StringDecoder());
					pipeline.addLast("1",new InboundA());
                    pipeline.addLast("3",new InboundB());
                    pipeline.addLast("6",new InboundC());
                    pipeline.addLast("encoder",new StringEncoder());
                    pipeline.addLast("2",new OutboundA());
                    pipeline.addLast("4",new OutboundB());
                    pipeline.addLast("5",new OutboundC());
				};
				@Override
				public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
					super.handlerAdded(ctx);
					System.out.println("channel加入进来了: "+ctx.channel());
					ctxs.add(ctx);
				}
				@Override
				public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
					super.handlerRemoved(ctx);
					System.out.println("channel关闭了: "+ctx.channel());
					ctxs.remove(ctx);
				}
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
	private static class InboundA extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("InboundA read "+ msg.getClass() + " " + msg);
            super.channelRead(ctx, msg);
        }
    }

    private static class InboundC extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("InboundC read " + msg.getClass() + " " + msg);
            super.channelRead(ctx, msg);
            // 这样会从当前的handler向前找outbound
            //ctx.writeAndFlush(buffer);
        }
    }

    private static class OutboundA extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("OutboundA write");
            super.write(ctx, msg, promise);
        }
    }

    private static class OutboundB extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("OutboundB write");
            super.write(ctx, msg, promise);
        }
    }

    private static class OutboundC extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("OutboundC write");
            super.write(ctx, msg, promise);
        }
    }
}
