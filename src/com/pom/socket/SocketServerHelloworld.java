package com.pom.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class SocketServerHelloworld {
	public static void main(String[] args) throws IOException {
		int port = 8080;
		try(ServerSocketChannel channel = ServerSocketChannel.open();
				Selector selector = Selector.open()){
			channel.configureBlocking(false);
			channel.bind(new InetSocketAddress(port));
			
			System.out.println("server started "+ port);
			channel.register(selector, SelectionKey.OP_ACCEPT); //服务器channel仅注册accept
			
			Thread mainThread = Thread.currentThread();
			new Thread(()->{
				boolean exit =false;
				while(!exit) {
					Debugger.sleep(5000);
					try {
						Debugger.file(mainThread);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}) .start();

			while(selector.select() > 0) {
				Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
				while(iterator.hasNext()) {
					SelectionKey key = iterator.next();
					if(key.isAcceptable()) {
						SocketChannel clientChannel = ((ServerSocketChannel)key.channel()).accept();
						clientChannel.configureBlocking(false);
						
						System.out.println("accept...");
					}
					iterator.remove();
				}
			}
			
		}
	}
}



