package com.github.overlogic.util.concurrent.actor;

import com.github.overlogic.util.concurrent.ExpirableTask;
import com.github.overlogic.util.concurrent.Updatable;

public final class UpdateTask implements ExpirableTask {
	private final Updatable updatable;
	
	public UpdateTask(final Updatable updatable) {
		this.updatable = updatable;
	}	
	
	public Updatable updatable() {
		return this.updatable;
	}
	
	@Override
	public void execute(final long delta) throws Exception {
		this.updatable.update(delta);
	}

	@Override
	public boolean expired() {
		return false;
	}
}
