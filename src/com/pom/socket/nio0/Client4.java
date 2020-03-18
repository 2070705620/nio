package com.pom.socket.nio0;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @功能说明:
 * @创建者: Pom
 * @创建时间: 2020-03-11 11:39:10
 * @公司名称: 180830.com
 * @版本:V1.0
 */
public class Client4 {
	public static void main(String[] args) {
		Debugger.startAndJoin(()->{
			System.out.println();
		});
	}
	public static void request() throws Exception {
		new Thread(()->{
			try {
				ByteBuffer readBuf = ByteBuffer.allocate(1024);
				ByteBuffer writeBuf = ByteBuffer.allocate(1024);
				SocketChannel channel = SocketChannel.open();
				channel.configureBlocking(false);
				Selector selector = Selector.open();
				channel.register(selector, SelectionKey.OP_CONNECT);
				
				channel.connect(new InetSocketAddress("127.0.0.1", 8013));
				
				while(selector.select() > 0) {
					Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
					while(keys.hasNext()) {
						SelectionKey key = keys.next();
						keys.remove();
						if(key.isConnectable()) {
							SocketChannel ch = (SocketChannel)key.channel();
							if(ch.isConnectionPending()) {
								ch.finishConnect();
								System.out.println("handshake2 finish");
								
								writeBuf.clear();
								writeBuf.put("handshake3 start".getBytes());
								writeBuf.flip();
								ch.write(writeBuf);
								
								Debugger.set(444, ch);
								
								ch.register(selector, SelectionKey.OP_READ);
								
							}
						}
						if(key.isReadable()) {
							SocketChannel ch = (SocketChannel)key.channel();
							ch.read(readBuf);
							readBuf.flip();
							System.out.println("response:" + new String(readBuf.array(), 0, readBuf.limit()));
							readBuf.clear();
						}
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}
}
