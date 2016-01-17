package com.github.overlogic.util.concurrent.actor;

import com.github.overlogic.util.concurrent.ExpirableTask;
import com.github.overlogic.util.concurrent.Updatable;

public final class UpdateObjectTask implements ExpirableTask {
	private final Updatable object;
	
	public UpdateObjectTask(final Updatable object) {
		this.object = object;
	}	
	
	public Updatable object() {
		return this.object;
	}
	
	@Override
	public void execute(final long delta) throws Exception {
		this.object.update(delta);
	}

	@Override
	public boolean expired() {
		return false;
	}
}
