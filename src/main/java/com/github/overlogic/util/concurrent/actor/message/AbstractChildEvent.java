package com.github.overlogic.util.concurrent.actor.message;

import com.github.overlogic.util.concurrent.actor.AbstractActor;

public abstract class AbstractChildEvent extends AbstractMessage {	
	
	private final ChildEventType type;
	
	private final AbstractActor child;	
	
	public AbstractChildEvent(final ChildEventType type, final AbstractActor child) {
		this.type = type;
		this.child = child;
	}	
	
	public ChildEventType type() {
		return this.type;
	}	
	
	public AbstractActor child() {
		return this.child;
	}
}	
