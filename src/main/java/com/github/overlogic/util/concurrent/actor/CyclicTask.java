package com.github.overlogic.util.concurrent.actor;

import java.util.concurrent.Callable;

public class CyclicTask extends Actor {
	
	private static final long NOT_STARTED = -1;
	private static final long SPIN_INFINITE = -1;
	
	private final Callable<Void> action;
	private final long delay;
	private long spin;
	private long nextExecution;

	public CyclicTask(final Callable<Void> action, final long delay) {
		this(action, delay, SPIN_INFINITE);
	}
	
	public CyclicTask(final Callable<Void> action, final long delay, final long spin) {
		this.action = action;
		this.delay = delay;
		this.spin = spin;
		this.nextExecution = NOT_STARTED;
	}

	private boolean started() {
		return this.nextExecution != NOT_STARTED;
	}
		
	private void scheduleNextExecution() {
		this.nextExecution += this.delay;		
	}
	
	private boolean shouldExecute() {
		return this.cumulatedTime >= this.nextExecution;
	}
		
	private void killIfSpinCompleted() {
		if(this.spin > 0) {
			this.spin--;
			if(this.spin == 0) {
				this.completed = true;
			}
		}	
	}
		
	@Override
	public void execute(final long delta) throws Exception {	
		super.execute(delta);
		if(this.started()) {
			if(this.shouldExecute()) {
				this.action.call();
				this.scheduleNextExecution();
				this.killIfSpinCompleted();
			}
		}
		else {
			scheduleNextExecution();
		}
	}
}
