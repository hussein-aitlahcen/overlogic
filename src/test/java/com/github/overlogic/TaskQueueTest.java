package com.github.overlogic;

import java.util.concurrent.atomic.AtomicInteger;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import com.github.overlogic.util.TimeSource;
import com.github.overlogic.util.concurrent.actor.CyclicTask;
import com.github.overlogic.util.concurrent.actor.OneshotTask;
import com.github.overlogic.util.concurrent.actor.TaskQueue;

public final class TaskQueueTest {
	
	@Test
	public void runningAfterStart() throws InterruptedException {
		final AtomicInteger value = new AtomicInteger(0);
		final TaskQueue queue = new TaskQueue("test-queue", 10, TimeSource.SYSTEM);
		queue.addChild(new CyclicTask(() -> {
			value.incrementAndGet();
		}, 100))
		.addChild(new OneshotTask(() -> {
			queue.abruptStop();
		}, 5000));
		queue.start();
		queue.synchronize();		
		MatcherAssert.assertThat("Value should have been incremented because the task is running", value.get() > 0);
	}
}
