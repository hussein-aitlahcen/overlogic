package com.github.overlogic.network;

import com.github.overlogic.util.concurrent.actor.Message;

public final class ClientEvent extends Message {
	private final ClientEventType type;
	private final Object client;
	public ClientEvent(final ClientEventType type, final Object client) {
		this.type = type;
		this.client = client;
	}
	public final ClientEventType type() {
		return this.type;
	}
	public final Object client() {
		return this.client;
	}
}
