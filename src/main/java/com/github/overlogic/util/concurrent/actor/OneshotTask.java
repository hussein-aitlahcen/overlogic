package com.github.overlogic.util.concurrent.actor;

public final class OneshotTask extends CyclicTask {	
	public static final int SINGLE_CYCLE = 1;
	
	public OneshotTask(final Runnable action, long delayInMs) {
		super(action, delayInMs, SINGLE_CYCLE);
	}
}
