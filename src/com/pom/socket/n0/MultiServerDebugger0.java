package com.pom.socket.n0;

import java.nio.channels.Selector;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoop;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.MultithreadEventExecutorGroup;

/**
 * @功能说明:
 * @创建者: Pom
 * @创建时间: 2020-03-10 16:04:21
 * @公司名称: 180830.com
 * @版本:V1.0
 */
public class MultiServerDebugger0 {
	/**
	 * com.pom.socket.n0.MultiServerDebugger0.t0()
	 */
	public static void t0() throws Exception {
		new Thread(()->{
			System.out.println("----------------------------------------");
			int i = 0;
			for(NioEventLoop eventLoop: MultiServer.eloops) {
				if(i++ == 1) {
					break;
				}
				Selector selector = DebuggerReflection.getField(eventLoop, "selector");
				Selector unwrappedSelector = DebuggerReflection.getField(eventLoop, "unwrappedSelector");
				String selStr = selector.toString();
				String unwrapSelStr = unwrappedSelector.toString();
				//System.out.println("selector:" + selector +" unwrappedSelector:" + unwrapSelStr.substring(unwrapSelStr.lastIndexOf('@')));
				Selector selImpl = DebuggerReflection.<Selector>getField(selector, "delegate");
				selImpl.wakeup();
			}
		}) .start();
		
	}
}
