package com.github.overlogic.util.concurrent.actor.message;

import com.github.overlogic.util.concurrent.ExpirableTask;

public abstract class AbstractChildEvent extends AbstractMessage {	
	
	private final ChildEventType type;
	
	private final ExpirableTask child;	
	
	public AbstractChildEvent(final ChildEventType type, final ExpirableTask child) {
		this.type = type;
		this.child = child;
	}	
	
	public ChildEventType type() {
		return this.type;
	}	
	
	public ExpirableTask child() {
		return this.child;
	}
}	
