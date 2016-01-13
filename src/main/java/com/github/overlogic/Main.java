package com.github.overlogic;

import com.github.overlogic.util.concurrent.TaskQueue;
import com.github.overlogic.util.concurrent.actor.Actor;
import com.github.overlogic.util.concurrent.actor.CyclicTask;
import com.github.overlogic.util.concurrent.actor.Kill;
import com.github.overlogic.util.concurrent.actor.OneshotTask;

public class Main {
	public static void main(String[] args) {
		TaskQueue queue = new TaskQueue("Salut", 5, () -> System.currentTimeMillis());
		queue.start();
		Actor x = new CyclicTask(() -> { System.out.println("crazy"); return null; }, 1000L);
		Actor y = new OneshotTask(() -> { System.out.println("one shot"); return null; }, 5000L);
		queue.addChild(x);
		queue.addChild(y);
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		x.send(Kill.Instance);
	}
}
