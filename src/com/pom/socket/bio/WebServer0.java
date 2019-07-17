package com.pom.socket.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Random;

public class WebServer0 {
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(8089);
		
		boolean exit = false;
		
		while(!exit) {
			Socket socket = serverSocket.accept();
			socket.setSoTimeout(10000); //超时唤醒InputStream read超过这个时间就唤醒
			System.out.println("accept: "+ socket);
			new Thread(()->{
				try(InputStream is = socket.getInputStream();
						InputStreamReader isr = new InputStreamReader(is);
						BufferedReader br = new BufferedReader(isr);
						OutputStream os = socket.getOutputStream()){
					System.out.println("准备读取数据");
					
					//Request获取数据
					String line = null;
					try {
						while((line = br.readLine()) != null){ //socket.shutdownOutput();就会返回null
							System.out.println(line);
							if(line.length() == 0) {
								System.out.println("结尾空行结束");
								break;
							}
						}
					}catch (SocketTimeoutException e) {
						System.out.println("读取超时");
					}
					//Response返回响应
					StringBuilder sb = new StringBuilder()
						.append("HTTP/1.1 200 OK\r\n")
						.append("Bdpagetype: 2\r\n")
						.append("Bdqid: 0xccf4c8a10005455a\r\n")
						.append("Cache-Control: private\r\n")
						.append("Connection: Keep-Alive\r\n")
//						.append("Content-Encoding: gzip\r\n")
						.append("Content-Type: text/html;charset=utf-8\r\n")
//						.append("Date: Mon, 15 Jul 2019 06:06:49 GMT\r\n")
//						.append("Expires: Mon, 15 Jul 2019 06:06:48 GMT\r\n")
						.append("Server: BWS/1.1\r\n")
						.append("Set-Cookie: BDSVRTM=516; path=/\r\n")
						.append("Set-Cookie: BD_HOME=1; path=/\r\n")
						.append("Set-Cookie: H_PS_PSSID=1433_21112_29522_29519_29237_28518_29099_28831_29221_26350_28701; path=/; domain=.baidu.com\r\n")
						.append("Strict-Transport-Security: max-age=172800\r\n")
						.append("X-Ua-Compatible: IE=Edge,chrome=1\r\n")
//						.append("Transfer-Encoding: chunked\r\n")
						.append("\r\n");
					sb.append("<html>");
					sb.append("<meta charset='UTF-8'>");
					sb.append("<body>");
					sb.append("<a href='https://www.baidu.com'>Baidu百度"+new Random().nextInt(10000)+"</a>");
					sb.append("</body>");
					sb.append("</html>");
					os.write(sb.toString().getBytes());
					os.flush();
					
					socket.shutdownOutput();
					
					//输入输出流可以正常关闭，只要不再传输数据
					is.close();
					os.close();
					socket.close();
					System.out.println("socket");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}).start();
		}
		serverSocket.close();
	}
}
