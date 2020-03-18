package sun.nio.ch;

import sun.nio.ch.WindowsSelectorImpl;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import sun.nio.ch.SocketChannelImpl;

/**
 * @功能说明:
 * @创建者: Pom
 * @创建时间: 2020-03-11 22:34:22
 * @公司名称: 180830.com
 * @版本:V1.0
 */
public class ChannelDebugger2 {
	/**
	 * sun.nio.ch.ChannelDebugger.t0()
	 */
	public static void t0() throws Exception {
		new Thread(()->{
			try {
				//遍历所有selector，打印它们各自Hold的channel
				//本例子有6个selector，有6堆channels
				for(int i=0; i < 6; i++) {
					Object selector = Debugger.get("s" + i);
					SelectionKeyImpl[] keys = DebuggerReflection.getField(selector, "channelArray");
					for(SelectionKeyImpl key: keys) {
						if(key != null) {
							if(key.channel().getClass().getName().equals("sun.nio.ch.SocketChannelImpl")) {
								InetSocketAddress address = DebuggerReflection.getField(key.channel(), "sun.nio.ch.SocketChannelImpl.remoteAddress");
								System.out.println(address.getPort());
							}	
						}
					}
					System.out.println("--------------------------");
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}) .start();
	}
}
