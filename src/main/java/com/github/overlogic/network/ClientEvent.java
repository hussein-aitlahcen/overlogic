package com.github.overlogic.network;

import com.github.overlogic.util.concurrent.actor.message.AbstractMessage;

public final class ClientEvent<T extends Client<T>> extends AbstractMessage {
	private final ClientEventType type;
	private final T client;
	public ClientEvent(final ClientEventType type, final T client) {
		this.type = type;
		this.client = client;
	}
	public final ClientEventType type() {
		return this.type;
	}
	public final T client() {
		return this.client;
	}
}
