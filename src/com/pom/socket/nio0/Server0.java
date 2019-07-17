package com.pom.socket.nio0;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Random;

public class Server0 {
	public static void main(String[] args) throws IOException{
		try(ServerSocketChannel serverChannel = ServerSocketChannel.open();
				){
			serverChannel.bind(new InetSocketAddress(
					8089), 10);
			serverChannel.configureBlocking(false);
			
			Selector selector = Selector.open();
			System.out.println(serverChannel.register(selector, SelectionKey.OP_ACCEPT));
			
			while(selector.select() > 0) {
				Iterator<SelectionKey> iteratorKeys = selector.selectedKeys().iterator();
				while(iteratorKeys.hasNext()) {
					SelectionKey key = iteratorKeys.next();
					iteratorKeys.remove();
					if(key.isAcceptable()) {
						SocketChannel channel = ((ServerSocketChannel)key.channel()).accept();
						System.out.println("accept的:"+key);
						channel.configureBlocking(false);
						channel.register(selector, SelectionKey.OP_READ);
					}
					if(key.isReadable()) {
						SocketChannel channel = (SocketChannel)key.channel();
						System.out.println("readable的:"+key);
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						while(channel.read(buffer) > 0) {
							buffer.flip();
							System.out.println(new String(buffer.array() ,0 ,buffer.limit()));
							buffer.clear();
						}
						
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
							
							ByteBuffer buffer = ByteBuffer.wrap(sb.toString().getBytes());
							channel.write(buffer);
							
							channel.close();
//							channel.register(selector, SelectionKey.OP_READ);
					}
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
