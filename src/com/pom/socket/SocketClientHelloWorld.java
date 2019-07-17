package com.pom.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class SocketClientHelloWorld {
	public static void main(String[] args) throws IOException {
		
		try(SocketChannel channel = SocketChannel.open();
				Selector selector = Selector.open()){
			channel.configureBlocking(false);
			//channel.register(selector, SelectionKey.OP_CONNECT);
			channel.connect(new InetSocketAddress("127.0.0.1", 8080));
			
		}
	}
}
