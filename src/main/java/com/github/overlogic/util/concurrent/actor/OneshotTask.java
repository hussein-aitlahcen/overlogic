package com.github.overlogic.util.concurrent.actor;

import java.util.concurrent.Callable;

public final class OneshotTask extends CyclicTask {	
	public OneshotTask(final Callable<Void> action, long delay) {
		super(action, delay, 1);
	}
}
