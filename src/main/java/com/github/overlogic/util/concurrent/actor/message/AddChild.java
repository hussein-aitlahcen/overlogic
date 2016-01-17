package com.github.overlogic.util.concurrent.actor.message;

import com.github.overlogic.util.concurrent.actor.AbstractActor;

public final class AddChild extends AbstractChildEvent {
	
	public AddChild(final AbstractActor child) {
		super(ChildEventType.ADD, child);
	}
	
}	
