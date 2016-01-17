package com.github.overlogic.util.concurrent.actor;

import com.github.overlogic.util.concurrent.ExpirableTask;
import com.github.overlogic.util.concurrent.ExpirableUpdatable;

public final class UpdateExpirableObjectTask implements ExpirableTask {
	
	private final ExpirableUpdatable updatable;
	
	public UpdateExpirableObjectTask(final ExpirableUpdatable updatable) {
		this.updatable = updatable;
	}	
	
	public ExpirableUpdatable updatable() {
		return this.updatable;
	}
	
	@Override
	public void execute(final long delta) throws Exception {
		this.updatable.update(delta);
	}

	@Override
	public boolean expired() {
		return this.updatable.expired();
	}
	
}
