package com.pom.socket.websocket;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @功能说明:
 * @创建者: Pom
 * @创建时间: 2020-03-19 10:37:44
 * @公司名称: 180830.com
 * @版本:V1.0
 */
public interface ServerDebugger1 {
	/**
	 * com.pom.socket.websocket.ServerDebugger1.t0()
	 */
	public static void t0() throws Exception {
//		Debugger.GLOBAL_DATA.size();

		for(int i=0; i < 10; i ++) {
			Debugger.set(RandomStringUtils.randomAlphanumeric(100), new byte[1024 * 10]) ;
		}
	}
	/**
	 * com.pom.socket.websocket.ServerDebugger.t1()
	 */
	public static void t1() throws Exception {
		Map<String, Object> body = new HashMap<>();
		body.put("name0", "张三");
		body.put("name1", "李四");
		body.put("age", 20 + new Random().nextInt(30));
		body.put("balance", 100 + new Random().nextFloat() * 10000);
		String json = new ObjectMapper().writeValueAsString(body);
		ChannelSupervise.send2All(new io.netty.handler.codec.http.websocketx.TextWebSocketFrame(json));
		
	}
}
