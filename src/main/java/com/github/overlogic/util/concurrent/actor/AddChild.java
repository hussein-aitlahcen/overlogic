package com.github.overlogic.util.concurrent.actor;

public final class AddChild extends ChildEvent {
	public AddChild(final Actor child) {
		super(ChildEventType.ADD, child);
	}
}