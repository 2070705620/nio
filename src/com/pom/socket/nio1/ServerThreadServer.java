package com.pom.socket.nio1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ServerThreadServer {
	public static void main(String[] args) throws IOException{
		Executor executors = Executors.newFixedThreadPool(6); 
		Executor guestExecutor = Executors.newScheduledThreadPool(3);
		Debugger.set("guestExecutor", guestExecutor);
		try(ServerSocketChannel serverChannel = ServerSocketChannel.open();
				){
			serverChannel.bind(new InetSocketAddress(
					8089), 10);
			serverChannel.configureBlocking(false);
			
			Debugger.startDaemon(()->{
				ServerSocketChannel _serverChannel = serverChannel;
				System.out.println();
			});
			
			NioSelector[] selectors = new NioSelector[6];
			for(int i=0; i < 1; i++) {
				Selector selector = Selector.open();
				serverChannel.register(selector, SelectionKey.OP_ACCEPT);
				selectors[i] = new NioSelector(selector);
				executors.execute(selectors[i]);
				Debugger.set("s" + i, selector);
			}
			Debugger.set("selectors", selectors);
			Debugger.sleep(10000000);
			
		}
	}
}
