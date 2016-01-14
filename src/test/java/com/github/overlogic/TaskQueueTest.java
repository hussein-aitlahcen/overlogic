package com.github.overlogic;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import com.github.overlogic.util.TimeSource;
import com.github.overlogic.util.concurrent.actor.TaskQueue;

public final class TaskQueueTest {
	
	@Test(timeout = 10000)
	public void gracefulStop() throws InterruptedException {
		new TaskQueue("test-queue", 10, TimeSource.SYSTEM)
				.doStart()
				.doStop()
				.doSynchronize();	
		MatcherAssert.assertThat("TaskQueue.doStop should stop the Queue", true);
	}	
}
