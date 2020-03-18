package com.pom.socket.n3;

/**
 * @功能说明:
 * @创建者: Pom
 * @创建时间: 2020-03-16 00:03:14
 * @公司名称: 180830.com
 * @版本:V1.0
 */
public interface MessageConfig {
	/**
	 * 魔数占 2 bytes
	 * */
	public static short MAGIC = (short)0xCAFE;
	
	/**
	 * 魔数 2 bytes
	 * 长度占 2 bytes
	 * body offset = 4
	 * */
	public static byte BASE_LENGTH = 2 + 2;
	
}
