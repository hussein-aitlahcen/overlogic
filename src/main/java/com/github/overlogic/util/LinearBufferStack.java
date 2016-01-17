package com.github.overlogic.util;


public final class LinearBufferStack {
	
	private final int chunkSize;
	private final int chunkCount;
	private byte[] buffer;
	
	public LinearBufferStack(final int chunkSize, final int chunkCount) {
		this.chunkSize = chunkSize;
		this.chunkCount = chunkCount;
		this.buffer = new byte[chunkSize * chunkCount];
	}	
	
	public int chunkSize() {
		return this.chunkSize;
	}
	
	public int chuckCount() {
		return this.chunkCount;
	}
	
	public byte[] buffer() {
		return this.buffer;
	}
	
}
