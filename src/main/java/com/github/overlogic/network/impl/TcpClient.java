package com.github.overlogic.network.impl;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import com.github.overlogic.network.ClientEvent;
import com.github.overlogic.network.ClientEventType;
import com.github.overlogic.util.Observable;
import com.github.overlogic.util.concurrent.actor.Actor;
import com.github.overlogic.util.concurrent.actor.Message;

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
		System.out.println("client::completed read=" + bytesRead);
		if(bytesRead > 0) {
			byte[] chunk = new byte[bytesRead];
			this.buffer.position(0);
			this.buffer.get(chunk);
			this.buffer.position(0);
			this.send(new DataChunk(ByteBuffer.wrap(chunk)));
			this.beginRead();
		}
		else {
			this.disconnected();
		}
	}
	
	@Override
	public void failed(final Throwable exc, final Void attachment) {
		this.disconnected();
	}  
	
	private void disconnected() {
		System.out.println("client::disconnected");
		final Message disconnectedMsg = new ClientEvent(ClientEventType.DISCONNECTED, this);
		this.notifyObservers(observer -> {
			observer.send(disconnectedMsg);
		});
	}
}
