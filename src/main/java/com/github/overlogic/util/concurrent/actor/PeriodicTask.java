package com.github.overlogic.util.concurrent.actor;

public final class PeriodicTask  extends AbstractCyclicTask {		
	public PeriodicTask(final Runnable action, long delayInMs, long retryCount) {
		super(action, delayInMs, retryCount);
	}
}
