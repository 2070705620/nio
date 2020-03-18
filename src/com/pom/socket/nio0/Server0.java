package com.pom.socket.nio0;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server0 {
	@SuppressWarnings("restriction")
	public static void main(String[] args) throws IOException{
		try(ServerSocketChannel serverChannel = ServerSocketChannel.open();
				){
			serverChannel.bind(new InetSocketAddress(
					8089), 10);
			serverChannel.configureBlocking(false);
			
			ByteBuffer readBuffer = ByteBuffer.allocate(1024);
			ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
			
			Selector selector = Selector.open();
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
			
			Debugger.startDaemon(()->{
				System.out.println();
			});

			while(selector.select() > 0) {
				Iterator<SelectionKey> iteratorKeys = selector.selectedKeys().iterator();
				while(iteratorKeys.hasNext()) {
					SelectionKey key = iteratorKeys.next();
					System.out.println("key: readable["+key.isReadable()+"] writable["+key.isWritable()+"]");
					iteratorKeys.remove();
					if(key.isAcceptable()) {
						SocketChannel channel = ((ServerSocketChannel)key.channel()).accept();
						channel.configureBlocking(false);
						channel.register(selector, SelectionKey.OP_READ);
						System.out.println("handshake1 over");
						Debugger.set(333, channel);
					}
					if(key.isReadable()) {
						SocketChannel channel = (SocketChannel)key.channel();
						if(channel.read(readBuffer) == -1) {
							channel.close();
							System.out.println("客户端关闭");
							continue; //已经close的不能再做其它判断
						}else {
							readBuffer.flip();
							String body = new String(readBuffer.array() ,0 ,readBuffer.limit());
							System.out.println("body: " + body);
							readBuffer.clear();
							
							writeBuffer.clear();
							writeBuffer.put((body + org.apache.commons.lang3.RandomStringUtils.randomNumeric(10)).getBytes());
							writeBuffer.flip();
							channel.write(writeBuffer);
							
						}
//						if(Server0Helper.read(selector, (SocketChannel)key.channel(), readBuffer)) {
//							continue;
//						}
					}
//					if(key.isWritable()) {
//						Server0Helper.write(selector, (SocketChannel)key.channel(), writeBuffer);
//					}
				}
			}
			
			//3次握手
			/*SocketChannel socketChannel = serverChannel.accept();
			System.out.println(socketChannel);
			ByteBuffer buffer = ByteBuffer.allocate(2);
			while(socketChannel.read(buffer) > -1) { //read是阻塞的
				System.out.print(new String(buffer.array()));
				buffer.flip();
			}
			socketChannel.shutdownInput();
			socketChannel.close();*/
		}
	}
}
