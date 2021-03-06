package com.github.overlogic.util.concurrent.actor;

public final class OneshotTask extends AbstractCyclicTask {	
	
	public static final int NO_DELAY = 0;
	public static final int SINGLE_CYCLE = 1;
	
	public OneshotTask(final Runnable action) {
		this(action, NO_DELAY);
	}
	
	public OneshotTask(final Runnable action, long delayInMs) {
		super(action, delayInMs, SINGLE_CYCLE);
	}
	
}
