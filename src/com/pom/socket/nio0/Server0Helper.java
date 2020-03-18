package com.pom.socket.nio0;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @功能说明:
 * @创建者: Pom
 * @创建时间: 2020-03-11 12:00:34
 * @公司名称: 180830.com
 * @版本:V1.0
 */
public interface Server0Helper {
	public static boolean read(Selector selector, SocketChannel channel, ByteBuffer buffer) throws IOException{
		//-1就是关闭
		if(channel.read(buffer) == -1) {
			channel.close();
			System.out.println("客户端关闭");
			return true; //已经close的不能再做其它判断
		}else {
			buffer.flip();
			String body = new String(buffer.array() ,0 ,buffer.limit());
			System.out.println("body: " + body);
			buffer.clear();
			
			channel.register(selector, SelectionKey.OP_WRITE);
			return false;
		}
	}
	public static void write(Selector selector, SocketChannel channel, ByteBuffer buffer) throws IOException {
		buffer.clear();
		buffer.put("just ok ?".getBytes());
		buffer.flip();
		channel.write(buffer);
		channel.register(selector, SelectionKey.OP_READ);
	}
}
