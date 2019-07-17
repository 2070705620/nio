package com.pom.socket.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Hanshake0 {

	public static void main(String[] args) throws IOException {
		Socket socket = new Socket("192.168.100.4", 8089);
		OutputStream os = socket.getOutputStream();
//		boolean exit = false;
//		while(!exit) {
			os.write("helloworld".getBytes());
//		}
		System.out.println();
//		Debugger.sleep(100000000);
		socket.close();
	}

}
