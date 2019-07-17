package com.pom.socket.nio0;

import java.io.IOException;
import java.nio.channels.Selector;

public class Selector0 {
	public static void main(String[] args) {
		try {
			System.out.println(Selector.open());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
