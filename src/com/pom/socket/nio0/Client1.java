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

public class Client1 {
	private int bufferSize = 4096;
	private ByteBuffer readBuffer = ByteBuffer.allocate(bufferSize);
	private ByteBuffer writeBuffer = ByteBuffer.allocate(bufferSize);
	private SocketChannel channel = null;
	private Selector selector = null;
	
	private boolean runnig = true;
	
	public Client1(String ip, int port) throws IOException {
		channel = SocketChannel.open();
		channel.configureBlocking(false);
		selector = Selector.open();
		
		channel.connect(new InetSocketAddress(ip, port));
		
		
		System.out.println("连接？");
		
		channel.register(selector, SelectionKey.OP_CONNECT);
		
		
	}
	public void connect() throws IOException {
		while(runnig) {
			if(selector.select() > 0) {
				Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
				while(iterator.hasNext()) {
					SelectionKey key = iterator.next();
					iterator.remove();
					if(key.isConnectable()) {
						//绑定了SelectionKey.OP_CONNECT
						SelectableChannel keyChannel = key.channel(); 
						assert channel == keyChannel; 
						if(channel.isConnectionPending()) {
							channel.finishConnect();
							System.out.println("完成连接");
							Debugger.set(80, channel);
							
							writeBuffer.clear();
							writeBuffer.put(("连接完成，顺便发送一个request:" + new Random().nextInt(10000)).getBytes());
							writeBuffer.flip();
							channel.write(writeBuffer);
							
							channel.register(selector, SelectionKey.OP_READ);
							
						}
					}
					if(key.isReadable()) {
						SelectableChannel keyChannel = key.channel();
						assert channel == keyChannel;
						int readSize = -1;
						readBuffer.clear();
						while((readSize = channel.read(readBuffer)) > 0) {
							readBuffer.flip();
							System.out.println(new String(readBuffer.array(), 0, readBuffer.limit()));
							readBuffer.clear();
						}/*
						if(readSize <=0) {
							runnig = false;
						}*/
					}
					if(key.isWritable()) {
						System.out.println("服务器的读触发的写");
					}
					/*if(key.isWritable()) {
						SelectableChannel keyChannel = key.channel();
						assert channel == keyChannel; 
						
						writeBuffer.clear();
						writeBuffer.put(("send request:" + new Random().nextInt(10000)).getBytes());
						writeBuffer.flip();
						channel.write(writeBuffer);
						
						channel.register(selector, SelectionKey.OP_READ);
					}*/
				}
			}
		}
		channel.close(); //退出循环后关闭
	}
	
	public static void main(String[] args) throws IOException{
		Client1 client1 = new Client1("localhost", 8090);
		client1.connect();
	}
}
