package com.github.overlogic.util.concurrent.actor;

public final class OneshotTask extends AbstractCyclicTask {	
	
	public static final int DELAY_INSTANT = 0;
	public static final int SINGLE_CYCLE = 1;
	
	public OneshotTask(final Runnable action) {
		this(action, DELAY_INSTANT);
	}
	
	public OneshotTask(final Runnable action, long delayInMs) {
		super(action, delayInMs, SINGLE_CYCLE);
	}
	
}
