package com.github.overlogic.network.impl;

import java.nio.ByteBuffer;

import com.github.overlogic.util.concurrent.actor.Message;

public final class DataChunk extends Message {
	private final ByteBuffer chunk;
	public DataChunk(final ByteBuffer chunk) {
		this.chunk = chunk;
	}
	public final ByteBuffer chunk() {
		return this.chunk;
	}
}
