package com.github.overlogic.network.impl;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import com.github.overlogic.util.Observable;
import com.github.overlogic.util.concurrent.actor.Actor;

public abstract class TcpClient<T extends TcpClient<T>> extends Actor implements CompletionHandler<Integer, Void>, Observable<Actor> {
	
	private final ByteBuffer buffer;
	private final AsynchronousSocketChannel channel;
	
	public TcpClient(ByteBuffer buffer, AsynchronousSocketChannel channel) {
		this.channel = channel;
		this.buffer = buffer;
		this.beginRead();
	}
	
	private void beginRead() {
		this.channel.read(this.buffer, null, this);
	}
	
	@Override
	public void completed(final Integer bytesRead, final Void attachment) {
		if(bytesRead > 0) {
			byte[] chunk = new byte[bytesRead];
			this.buffer.get(chunk);
			this.send(new DataChunk(ByteBuffer.wrap(chunk)));
			this.beginRead();
		}
	}
	
	@Override
	public void failed(final Throwable exc, final Void attachment) {
		
	}  
}
