package com.github.overlogic.network.message;

import com.github.overlogic.network.AbstractClient;
import com.github.overlogic.util.concurrent.actor.message.AbstractMessage;

public final class ClientEvent<T extends AbstractClient<T>> extends AbstractMessage {
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
