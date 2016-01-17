package com.github.overlogic.util.concurrent.actor.message;

import com.github.overlogic.util.concurrent.actor.AbstractActor;

public final class RemoveObserver extends AbstractMessage {
	
	private final AbstractActor observer;	
	
	public RemoveObserver(final AbstractActor observer) {
		this.observer = observer;
	}	
	
	public AbstractActor observer() {
		return this.observer;
	}
	
}
