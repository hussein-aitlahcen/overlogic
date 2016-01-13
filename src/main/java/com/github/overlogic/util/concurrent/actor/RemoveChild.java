package com.github.overlogic.util.concurrent.actor;

public final class RemoveChild extends ChildEvent {
	public RemoveChild(Actor child) {
		super(ChildEventType.REMOVE, child);
	}
}
