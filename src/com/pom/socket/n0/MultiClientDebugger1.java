package com.pom.socket.n0;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.RandomStringUtils;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;

/**
 * @功能说明:
 * @创建者: Pom
 * @创建时间: 2020-03-12 22:11:06
 * @公司名称: 180830.com
 * @版本:V1.0
 */
public class MultiClientDebugger1 {
	/**
	 * com.pom.socket.n0.MultiClientDebugger1.t0()
	 */
	public static void t0() throws Exception {
		Bootstrap bootstrap = MultiClient.bootstrap;
		Channel[] channels = MultiClient.channels;
		System.out.println(channelIdx+" - " +nextChannel());
	}
	private static AtomicInteger channelIdx = new AtomicInteger(0);
	public static Channel nextChannel() {
		Channel[] channels = MultiClient.channels;
		//为了超过上限时变负数，所以需要abs
		return channels[Math.abs(channelIdx.getAndIncrement() % channels.length )];
	}
}
