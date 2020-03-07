package com.pom.socket.nio0;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Client3 {
	private int bufferSize = 4096;
	private ByteBuffer readBuffer = ByteBuffer.allocate(bufferSize);
	private ByteBuffer writeBuffer = ByteBuffer.allocate(bufferSize);
	private SocketChannel channel = null;
	private Selector selector = null;
	
	private boolean running = true;
	
	public Client3(String ip, int port) throws IOException {
		channel = SocketChannel.open();
		channel.configureBlocking(false);
		selector = Selector.open();
		
		channel.connect(new InetSocketAddress(ip, port));
		
		System.out.println("连接？");
		
		channel.register(selector, SelectionKey.OP_CONNECT);
		
		select: while(selector.select() > 0) {
			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
			while(iterator.hasNext()) {
				SelectionKey key = iterator.next();
				iterator.remove();
				if(key.isConnectable()) {
					SocketChannel channel = (SocketChannel)key.channel();
					if(channel.isConnectionPending()) {
						channel.finishConnect();
						System.out.println(channel);
						channel.write(ByteBuffer.wrap(("GET fdshj"+new Random().nextInt(10000)).getBytes()));
						
						channel.register(selector, SelectionKey.OP_READ);
						
					}
				}
				if(key.isReadable()) {
					ByteBuffer readBuffer = ByteBuffer.allocate(4096);
					channel.read(readBuffer);
					readBuffer.flip();
					System.out.println("response: "+ new String(readBuffer.array(), 0, readBuffer.limit()));
					
					channel.shutdownInput();
					channel.shutdownOutput();
					channel.close();
					Debugger.file("客户端自己关闭"+channel);
					
					break select;
				}
			}
			
		}
		
		
	}
	
	public static void main(String[] args) throws IOException{
		Client3 client1 = new Client3("localhost", 8090);
	}
}
