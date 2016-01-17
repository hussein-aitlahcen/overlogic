package com.github.overlogic.network.message;

import java.nio.ByteBuffer;

import com.github.overlogic.util.concurrent.actor.message.AbstractMessage;

public final class DataChunk extends AbstractMessage {
	
	private final ByteBuffer chunk;
	
	public DataChunk(final ByteBuffer chunk) {
		this.chunk = chunk;
	}
	
	public final ByteBuffer chunk() {
		return this.chunk;
	}
	
}
