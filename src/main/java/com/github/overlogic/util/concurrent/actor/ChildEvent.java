package com.github.overlogic.util.concurrent.actor;

public abstract class ChildEvent extends Message {
	
	private final ChildEventType type;
	private final Actor child;
	
	public ChildEvent(final ChildEventType type, final Actor child) {
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