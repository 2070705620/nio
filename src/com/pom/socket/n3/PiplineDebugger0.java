package com.pom.socket.n3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @功能说明:
 * @创建者: Pom
 * @创建时间: 2020-03-15 22:55:52
 * @公司名称: 180830.com
 * @版本:V1.0
 */
public class PiplineDebugger0 {
	/**
	 * com.pom.socket.n3.PiplineDebugger0.t0()
	 */
	public static void t0() throws Exception {
		new Thread(()->{
			ByteBuf bb = Unpooled.buffer(10);
			System.out.println(bb);
		}) .start();
	}
}
