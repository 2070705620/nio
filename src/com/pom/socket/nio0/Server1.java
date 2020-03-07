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

public class Server1 {
	private ServerSocketChannel serverChannel = null;
	private int bufferSize = 4096;
	private ByteBuffer readBuffer = ByteBuffer.allocate(bufferSize);
	private ByteBuffer writeBuffer = ByteBuffer.allocate(bufferSize);
	private Selector selector = null;
	private boolean running = true;
	
	public Server1(int port) throws IOException {
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
			if(selector.select() > 0) {
				Iterator<SelectionKey> iteratorKeys = selector.selectedKeys().iterator();
				while(iteratorKeys.hasNext()) {
					SelectionKey key = iteratorKeys.next();
					iteratorKeys.remove();
					if(key.isAcceptable()) {
						SocketChannel channel = ((ServerSocketChannel)key.channel()).accept();
						System.out.println("accept: "+ channel);
						channel.configureBlocking(false);
						
						Debugger.set(2, channel);
						
						channel.register(selector, SelectionKey.OP_READ);
						
						
					}
					if(key.isReadable()) {
						SocketChannel channel = (SocketChannel)key.channel();
						//使用前先清洁一下
						readBuffer.clear();
						if(channel.read(readBuffer) > 0) {
							readBuffer.flip();
							System.out.println(new String(readBuffer.array() ,0 ,readBuffer.limit()));
							readBuffer.clear();
						}
//						channel.shutdownInput();
						channel.register(selector, SelectionKey.OP_WRITE);
					}
					if(key.isWritable()) {
						SocketChannel channel = (SocketChannel)key.channel();
						
						System.out.println("writable的:"+key);
						
						StringBuilder sb = new StringBuilder()
								.append("HTTP/1.1 200 OK\r\n")
								.append("Bdpagetype: 2\r\n")
								.append("Bdqid: 0xccf4c8a10005455a\r\n")
								.append("Cache-Control: private\r\n")
								.append("Connection: Keep-Alive\r\n")
//								.append("Content-Encoding: gzip\r\n")
								.append("Content-Type: text/html;charset=utf-8\r\n")
//								.append("Date: Mon, 15 Jul 2019 06:06:49 GMT\r\n")
//								.append("Expires: Mon, 15 Jul 2019 06:06:48 GMT\r\n")
								.append("Server: BWS/1.1\r\n")
								.append("Set-Cookie: BDSVRTM=516; path=/\r\n")
								.append("Set-Cookie: BD_HOME=1; path=/\r\n")
								.append("Set-Cookie: H_PS_PSSID=1433_21112_29522_29519_29237_28518_29099_28831_29221_26350_28701; path=/; domain=.baidu.com\r\n")
								.append("Strict-Transport-Security: max-age=172800\r\n")
								.append("X-Ua-Compatible: IE=Edge,chrome=1\r\n")
//								.append("Transfer-Encoding: chunked\r\n")
								.append("\r\n");
							sb.append("<html>");
							sb.append("<meta charset='UTF-8'>");
							sb.append("<body>");
							String simpleString = "";
							sb.append("<a href='https://www.baidu.com'>Baidu百度"+simpleString+"</a>");
							sb.append("</body>");
							sb.append("</html>");
							//response
							{
								byte[]data = sb.toString().getBytes();
								int length = data.length;
								int index = 0;
								while(index < length) {
									writeBuffer.clear();
									int nextOffset = Math.min(index + bufferSize, length);
									writeBuffer.put(data, index, nextOffset - index);
									writeBuffer.flip();
									channel.write(writeBuffer);
									index = nextOffset;
								}
//								channel.shutdownOutput();
							}
							channel.register(selector, SelectionKey.OP_READ);
					}
				}
			}
		}
	}
	public static void main(String[] args) throws IOException{
		new Server1(8090).listen();
	}
}
