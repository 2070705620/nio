package com.pom.socket.n1;

import java.util.concurrent.TimeUnit;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @功能说明:
 * @创建者: Pom
 * @创建时间: 2020-03-13 22:00:49
 * @公司名称: 180830.com
 * @版本:V1.0
 */
public class NioEventLoopTest {
	public static void main(String[] args) {
		EventLoopGroup eventLoop = new NioEventLoopGroup();
		eventLoop.next();
	}
}
