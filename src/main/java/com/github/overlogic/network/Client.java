package com.github.overlogic.network;

import com.github.overlogic.util.concurrent.actor.Actor;

public abstract class Client<T extends Client<T>> extends Actor {
	private final int identity;
	
	public Client(final int identity) {
		this.identity = identity;
	}
	
	public final int identity() {
		return this.identity;
	}
}
