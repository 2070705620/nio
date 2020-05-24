package com.pom.socket.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @功能说明:
 * @创建者: Pom
 * @创建时间: 2020-03-19 00:02:24
 * @公司名称: 180830.com
 * @版本:V1.0
 */
public class ChannelSupervise {
	 private   static ChannelGroup GlobalGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	    private  static Map<String, ChannelId> ChannelMap=new ConcurrentHashMap<>();
	    public  static void addChannel(Channel channel){
	        GlobalGroup.add(channel);
	        ChannelMap.put(channel.id().asShortText(),channel.id());
	    }
	    public static void removeChannel(Channel channel){
	        GlobalGroup.remove(channel);
	        ChannelMap.remove(channel.id().asShortText());
	    }
	    public static  Channel findChannel(String id){
	        return GlobalGroup.find(ChannelMap.get(id));
	    }
	    public static void send2All(TextWebSocketFrame tws){
	        GlobalGroup.writeAndFlush(tws);
	    }
	    public static void send2All(String message){
	        GlobalGroup.writeAndFlush(new TextWebSocketFrame(message));
	    }
}
