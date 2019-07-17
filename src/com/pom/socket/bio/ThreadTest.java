package com.pom.socket.bio;

import java.util.stream.IntStream;

public class ThreadTest {
	public static void main(String[] args) {
		IntStream.range(0, 100).forEach(i->{
			new Thread(()->{
				Debugger.sleep(10000);
			}) .start();
		});
		
	}
}
