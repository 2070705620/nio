package com.pom.socket.n3;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class InboundHandler1 extends ByteToMessageDecoder {
	
	private static final Logger logger = LoggerFactory.getLogger(InboundHandler1.class);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		logger.debug("{}", in.toString().substring(in.toString().lastIndexOf('(')));
		Debugger.set("in", in);
		if(in.readableBytes() >= MessageConfig.BASE_LENGTH) {
			/*暂不支持大数据包发送
			 * */
			//超过2048就认为是字节流攻击，废弃它
			if(in.readableBytes() > 2048) {
				in.skipBytes(in.readableBytes()); //理论上是应该返回错误信息
				return;	
			}
			
			while(true) {
				if(in.getShort(in.readerIndex()) == MessageConfig.MAGIC) {
					break;
				}
				in.skipBytes(1); //丢弃1byte
				if(in.readableBytes() < MessageConfig.BASE_LENGTH) {
					return; //长度不足够了，等待后面的内容
				}
			}
			int beginIndex = in.readerIndex();
			/*short magic = */in.readShort();	//到达这里肯定就是魔数了
			short length = in.readShort();
			System.out.println("body length:" + length);
			if(length > in.readableBytes()) {
				//等待后面的包
				in.readerIndex(beginIndex);
			}else { //长度足够
				byte[]body = new byte[length];
				in.readBytes(body);
				out.add(new String(body, "UTF-8"));
			}
		}else {
			//基本长度不够，等待后面的内容发送过来
		}
	}

}
