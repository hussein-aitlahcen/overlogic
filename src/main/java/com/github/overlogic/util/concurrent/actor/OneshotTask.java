package com.github.overlogic.util.concurrent.actor;

public final class OneshotTask extends CyclicTask {	
	public OneshotTask(final Runnable action, long delayInMs) {
		super(action, delayInMs, 1);
	}
}
