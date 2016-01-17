package com.github.overlogic.util.concurrent.actor.message;

import java.util.function.Consumer;

import com.github.overlogic.util.concurrent.actor.AbstractActor;

public final class NotifyObservers extends AbstractMessage {
	
	private final Consumer<AbstractActor> action;
	
	public NotifyObservers(final Consumer<AbstractActor> action) {
		this.action = action;
	}
	
	public final Consumer<AbstractActor> action() {
		return this.action;
	}
	
}
