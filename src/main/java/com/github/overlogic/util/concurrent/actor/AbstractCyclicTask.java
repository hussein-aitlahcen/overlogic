package com.github.overlogic.util.concurrent.actor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.overlogic.util.concurrent.ExpirableTask;

public abstract class AbstractCyclicTask implements ExpirableTask {

	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractCyclicTask.class);
	
	private static final long NOT_STARTED = -1;
	private static final long SPIN_INFINITE = -1;
	
	private final Runnable action;
	private final long delayInMs;
	private long cumulatedTime;
	private long spin;
	private long nextExecution;
	private boolean expired;

	public AbstractCyclicTask(final Runnable action, final long delayInMs) {
		this(action, delayInMs, SPIN_INFINITE);
	}
	
	public AbstractCyclicTask(final Runnable action, final long delayInMs, final long spin) {
		this.action = action;
		this.delayInMs = delayInMs;
		this.spin = spin;
		this.expired = false;
		this.cumulatedTime = 0;
		this.nextExecution = NOT_STARTED;
	}

	private boolean started() {
		return this.nextExecution != NOT_STARTED;
	}
		
	private void scheduleNextExecution() {
		this.nextExecution += this.delayInMs;		
	}
	
	private void accumulate(final long delta) {
		this.cumulatedTime += delta;
	}
	
	private boolean shouldExecute() {
		return this.cumulatedTime >= this.nextExecution;
	}
			
	private void checkCompletion() {
		if(this.spin != SPIN_INFINITE) {
			this.spin--;
			if(this.spin < 1) {
				this.expired = true;
			}
		}	
	}

	@Override
	public boolean expired() {
		return this.expired;
	}
	
	@Override
	public void execute(final long delta) throws Exception {	
		this.accumulate(delta);
		if(this.started()) {
			if(this.shouldExecute()) {
				this.action.run();
				this.scheduleNextExecution();
				this.checkCompletion();
			}
		}
		else {
			this.nextExecution = this.delayInMs;
		}
	}
	
}
