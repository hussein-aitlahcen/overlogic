package com.github.overlogic.util.concurrent.actor.message;

import com.github.overlogic.util.concurrent.actor.Actor;

public final class AddChild extends AbstractChildEvent {
	public AddChild(final Actor child) {
		super(ChildEventType.ADD, child);
	}
}	
