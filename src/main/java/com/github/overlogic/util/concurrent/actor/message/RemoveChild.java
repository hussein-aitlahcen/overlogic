package com.github.overlogic.util.concurrent.actor.message;

import com.github.overlogic.util.concurrent.actor.Actor;

public final class RemoveChild extends AbstractChildEvent {
	public RemoveChild(Actor child) {
		super(ChildEventType.REMOVE, child);
	}
}
