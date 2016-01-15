package com.github.overlogic;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import com.github.overlogic.util.concurrent.actor.ActorService;

public final class ActorServiceTest {	
	@Test(timeout = 5000)
	public void startStopAndSynchronize() throws Exception {
		new ActorService("test-service")
			.started()
			.stopped()
			.waitUntilFullyStopped();
		MatcherAssert.assertThat("ActorService should be stopped", true);
	}	
}
