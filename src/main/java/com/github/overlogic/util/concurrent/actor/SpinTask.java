package com.github.overlogic.util.concurrent.actor;

public abstract class SpinTask extends Actor {
	protected int spin;
	
	public SpinTask(final int spin) {
		this.spin = spin;
	}

	@Override
	public boolean expired() {
		return spin <= 0;
	}
	
	protected void decrement() {
		this.spin--;
	}
	
	protected void increment() {
		this.spin++;
	}
	
	@Override
	public abstract void execute(final long delta) throws Exception;	
}
