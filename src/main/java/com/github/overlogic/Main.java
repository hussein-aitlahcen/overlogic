package com.github.overlogic;

import com.github.overlogic.util.concurrent.TaskQueue;

public class Main {
	public static void main(String[] args) {
		TaskQueue queue = new TaskQueue("Salut", 5, () -> System.currentTimeMillis());
		queue.start();
		try {
			TestServer server = new TestServer("127.0.0.1", 80);
			server.listen();
			queue.addChild(server);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
