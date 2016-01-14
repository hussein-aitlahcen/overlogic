package com.github.overlogic.util.concurrent.actor.message;

import com.github.overlogic.util.concurrent.actor.Actor;

public abstract class AbstractChildEvent extends AbstractMessage {	
	private final ChildEventType type;
	private final Actor child;	
	public AbstractChildEvent(final ChildEventType type, final Actor child) {
		this.type = type;
		this.child = child;
	}	
	public ChildEventType type() {
		return this.type;
	}	
	public Actor child() {
		return this.child;
	}
}	
