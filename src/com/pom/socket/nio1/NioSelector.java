package com.pom.socket.nio1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.Executor;

/**
 * @功能说明:
 * @创建者: Pom
 * @创建时间: 2020-03-11 19:49:01
 * @公司名称: 180830.com
 * @版本:V1.0
 */
public class NioSelector implements Runnable{
	private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
	private ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
	private Selector selector;
	public NioSelector(Selector selector) {
		super();
		this.selector = selector;
	}
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName() + " 就绪");
		try {
			while(selector.select() > 0) {
				Iterator<SelectionKey> iteratorKeys = selector.selectedKeys().iterator();
				while(iteratorKeys.hasNext()) {
					SelectionKey key = iteratorKeys.next();
					iteratorKeys.remove();
					if(key.isAcceptable()) {
						SocketChannel channel = ((ServerSocketChannel)key.channel()).accept();
						if(channel != null) {
							System.out.println(Thread.currentThread().getName() + " - accepted: " + channel);
							channel.configureBlocking(false);
							channel.register(selector, SelectionKey.OP_READ);
							System.out.println("handshake1 over");
							Debugger.set(333, channel);
						}
					}
					if(key.isReadable()) {
						SocketChannel channel = (SocketChannel)key.channel();
						if(channel.read(readBuffer) == -1) {
							channel.close();
							System.out.println("客户端关闭");
							continue; //已经close的不能再做其它判断
						}else {
							Debugger.<Executor>get("guestExecutor")
								.execute(new GuestTask(readBuffer, writeBuffer, channel));
						}
					}
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + " 退出");
	}
}
