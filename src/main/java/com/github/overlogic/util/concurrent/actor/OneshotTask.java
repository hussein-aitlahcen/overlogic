package com.github.overlogic.util.concurrent.actor;

public final class OneshotTask extends CyclicTask {	
	public OneshotTask(final Runnable action, long delay) {
		super(action, delay, 1);
	}
}
