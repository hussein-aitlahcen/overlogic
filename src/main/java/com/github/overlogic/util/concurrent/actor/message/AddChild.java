package com.github.overlogic.util.concurrent.actor.message;

import com.github.overlogic.util.concurrent.ExpirableTask;

public final class AddChild extends AbstractChildEvent {
	
	public AddChild(final ExpirableTask child) {
		super(ChildEventType.ADD, child);
	}
	
}	
