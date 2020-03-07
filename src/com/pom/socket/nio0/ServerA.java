package com.pom.socket.nio0;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 适合处理WebHTTP
 * */
public class ServerA {
	private ServerSocketChannel serverChannel = null;
	private int bufferSize = 4096;
	private ByteBuffer readBuffer = ByteBuffer.allocate(bufferSize);
	private ByteBuffer writeBuffer = ByteBuffer.allocate(bufferSize);
	private Selector selector = null;
	private boolean running = true;
	
	public ServerA(int port) throws IOException {
		serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);
		//绑定端口
		ServerSocket serverSocket = serverChannel.socket();
		serverSocket.bind(new InetSocketAddress(port));
		//serverChannel.bind(new InetSocketAddress(port));
		
		System.out.println("服务器已经开启，端口:"+port);
		
		selector = Selector.open();
		
		//注册accept事件，注册主体是服务器channel
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		
		new Thread(()->{
			Debugger.set(1, 1);
			while(true) {
				Debugger.sleep(5000);
			}
		}) .start();
		
	}
	public void listen() throws IOException {
		while(running) {
			selector.select();
			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
			while(iterator.hasNext()) {
				SelectionKey key = iterator.next();
				iterator.remove();
				if(key.isAcceptable()) {
					keyAccept(key);
				}
				if(key.isReadable()) {
					keyReadable(key);
				}
				if(key.isWritable()) {
					keyWritable(key);
				}
			}
		}
		serverChannel.close();
	}
	private void keyAccept(SelectionKey key) throws IOException {
		SocketChannel channel = ((ServerSocketChannel)key.channel()).accept();
		if(channel != null) {
			channel.configureBlocking(false);
			channel.register(selector, SelectionKey.OP_READ); //读取来自客户端的请求
		}else {
			System.out.println("连接失败");
		}
		
	}
	private void keyReadable(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel)key.channel();
		readBuffer.clear();
		boolean hasContent = false;
		//数据读完之后为0，只有shutdownInput后才为-1
		while(channel.read(readBuffer) > 0) {
			hasContent = true;
			readBuffer.flip();
			System.out.println(new String(readBuffer.array(),0,readBuffer.limit()));
			readBuffer.clear();
		}
		if(hasContent) {
			channel.register(selector, SelectionKey.OP_WRITE); //读完毕，到写了
		}else{
			channel.close();
		}
	}
	private void keyWritable(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel)key.channel();
		ByteBuffer buffer = ByteBuffer.wrap(getResponseText().getBytes());
		while(buffer.hasRemaining()) {
			channel.write(buffer);
		}
		channel.close();
		System.out.println("response 完毕");
		
	}
	 private String getResponseText() {
         StringBuffer sb = new StringBuffer();
         sb.append("HTTP/1.1 200 OK\n");
         sb.append("Content-Type: text/html; charset=UTF-8\n");
         sb.append("\n");
         sb.append("<html>");
         sb.append("  <head>");
         sb.append("    <title>");
         sb.append("      NIO Http Server");
         sb.append("    </title>");
         sb.append("  </head>");
         sb.append("  <body>");
         sb.append("    <h1>Hello World!</h1>");
         sb.append("  </body>");
         sb.append("</html>");
         
         return sb.toString();
     }
	public static void main(String[] args) throws IOException{
		new ServerA(8090).listen();
	}
}


