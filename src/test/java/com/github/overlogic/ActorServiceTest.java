package com.github.overlogic;

import java.util.concurrent.atomic.AtomicBoolean;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import com.github.overlogic.util.concurrent.actor.ActorService;
import com.github.overlogic.util.concurrent.actor.OneshotTask;

public final class ActorServiceTest extends AbstractTest {		
	@Test(timeout = 2500)
	public void startStopAndSynchronize() throws Exception {
		final AtomicBoolean bit = new AtomicBoolean(false);
		final ActorService service = new ActorService("test-service");
		service.start();	
		service.addChild(new OneshotTask(() -> {
			bit.set(true);
		}, 2000));
		while(!bit.get()) {
			Thread.sleep(1);
		}
		service.stop();
		service.synchronize();
		MatcherAssert.assertThat("ActorService should be stopped", bit.get());
	}	
}
