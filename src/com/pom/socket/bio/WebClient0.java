package com.pom.socket.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.stream.IntStream;

public class WebClient0 {
	public static void main(String[] args){
//		IntStream.range(0, 100).forEach(_unbused_i->{
//			new Thread(()-> {
				try {
					Socket socket = new Socket("192.168.100.2", 8089);
					try(OutputStream os = socket.getOutputStream();
							InputStream is = socket.getInputStream()){
						StringBuilder sb = new StringBuilder();
						sb.append("GET /img/baidu_jgylogo3.gif HTTP/1.1\r\n");
						sb.append("ABCD\r\n");
						sb.append("EFGH\r\n");
						os.write(sb.toString().getBytes());
						os.flush();
						Debugger.sleep(10000);
						socket.shutdownOutput();
						
						byte[] buffer = new byte[1024];
						int offset = -1;
						while((offset = is.read(buffer)) > -1) {
							System.out.println(new String(buffer,0, offset));
						}
						
					}
					Debugger.sleep(5000);
					socket.close();
				}catch (IOException e) {
					e.printStackTrace();
				}
				Debugger.sleep(1000);
//			}).start();
			
//		});
	}
}
