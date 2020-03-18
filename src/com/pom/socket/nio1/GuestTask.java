package com.pom.socket.nio1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @功能说明:
 * @创建者: Pom
 * @创建时间: 2020-03-11 23:46:49
 * @公司名称: 180830.com
 * @版本:V1.0
 */
public class GuestTask implements Runnable{
	private ByteBuffer readBuffer = null;
	private ByteBuffer writeBuffer = null;
	private SocketChannel channel;
	public GuestTask(ByteBuffer readBuffer, ByteBuffer writeBuffer, SocketChannel channel) {
		super();
		this.readBuffer = readBuffer;
		this.writeBuffer = writeBuffer;
		this.channel = channel;
	}

	@Override
	public void run() {
		
		try {
			readBuffer.flip();
			String body = new String(readBuffer.array() ,0 ,readBuffer.limit());
			System.out.println("body: " + body);
			readBuffer.clear();
			
			Debugger.sleep(10000);
			
			writeBuffer.clear();
			writeBuffer.put((body + org.apache.commons.lang3.RandomStringUtils.randomNumeric(10)).getBytes());
			writeBuffer.flip();
			channel.write(writeBuffer);
			
			System.out.println(Thread.currentThread().getName() + " 返回");
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
