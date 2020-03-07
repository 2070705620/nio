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

public class Client2 {
	private int bufferSize = 4096;
	private ByteBuffer readBuffer = ByteBuffer.allocate(bufferSize);
	private ByteBuffer writeBuffer = ByteBuffer.allocate(bufferSize);
	private SocketChannel channel = null;
	private Selector selector = null;
	
	private boolean runnig = true;
	
	public Client2(String ip, int port) throws IOException {
		channel = SocketChannel.open();
		channel.configureBlocking(false);
		selector = Selector.open();
		
		channel.connect(new InetSocketAddress(ip, port));
		
		
		System.out.println("连接？");
		
		channel.register(selector, SelectionKey.OP_CONNECT);
		Debugger.set(2, channel);
		new Thread(()->{
			Debugger.set(1, 1);
			while(true) {
				Debugger.sleep(5000);
			}
		}) .start();
	}
	
	public static void main(String[] args) throws IOException{
		Client2 client1 = new Client2("localhost", 8090);
	}
}
