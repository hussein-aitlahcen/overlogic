package com.github.overlogic.util.concurrent.actor.message;

import com.github.overlogic.util.concurrent.actor.AbstractActor;

public final class AddObserver extends AbstractMessage {
	private final AbstractActor observer;	
	public AddObserver(final AbstractActor observer) {
		this.observer = observer;
	}	
	public AbstractActor observer() {
		return this.observer;
	}
}
