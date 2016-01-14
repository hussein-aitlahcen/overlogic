package com.github.overlogic.util.concurrent.actor;

public class CyclicTask extends Actor {
	
	private static final long NOT_STARTED = -1;
	private static final long SPIN_INFINITE = -1;
	
	private final Runnable action;
	private final long delayInMs;
	private long spin;
	private long nextExecution;

	public CyclicTask(final Runnable action, final long delayInMs) {
		this(action, delayInMs, SPIN_INFINITE);
	}
	
	public CyclicTask(final Runnable action, final long delayInMs, final long spin) {
		this.action = action;
		this.delayInMs = delayInMs;
		this.spin = spin;
		this.nextExecution = NOT_STARTED;
	}

	private boolean started() {
		return this.nextExecution != NOT_STARTED;
	}
		
	private void scheduleNextExecution() {
		this.nextExecution += this.delayInMs;		
	}
	
	private boolean shouldExecute() {
		return this.cumulatedTime >= this.nextExecution;
	}
		
	private void checkCompletion() {
		if(this.spin != SPIN_INFINITE) {
			this.spin--;
			if(this.spin < 1) {
				this.completed = true;
			}
		}	
	}
		
	@Override
	public void execute(final long delta) throws Exception {	
		super.execute(delta);
		if(this.started()) {
			if(this.shouldExecute()) {
				this.action.run();
				this.scheduleNextExecution();
				this.checkCompletion();
			}
		}
		else {
			scheduleNextExecution();
		}
	}
}
