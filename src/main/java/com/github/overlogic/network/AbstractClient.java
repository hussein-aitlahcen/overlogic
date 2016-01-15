package com.github.overlogic.network;

import com.github.overlogic.util.concurrent.actor.AbstractActor;

public abstract class AbstractClient<T extends AbstractClient<T>> extends AbstractActor {
	
	private final int identity;
	
	public AbstractClient(final int identity) {
		this.identity = identity;
	}
	
	public final int identity() {
		return this.identity;
	}
}
