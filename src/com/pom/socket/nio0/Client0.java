package com.pom.socket.nio0;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.stream.IntStream;

public class Client0 {

	public static void main(String[] args) {
		IntStream.range(0, 1000).forEach(i->{
			try {
				Socket socket = new Socket("127.0.0.1", 8089);
				try(OutputStream os = socket.getOutputStream()){
					
				}
				socket.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

}
