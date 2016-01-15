package com.github.overlogic;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import com.github.overlogic.util.TimeSource;
import com.github.overlogic.util.concurrent.actor.ActorService;

public final class ActorServiceTest {	
	@Test(timeout = 5000)
	public void startStopAndSynchronize() throws Exception {
		new ActorService("test-service", 10, TimeSource.SYSTEM)
				.doStart()
				.doStop()
				.doSynchronize();	
		MatcherAssert.assertThat("ActorService.doStop should stop the Queue", true);
	}	
}
