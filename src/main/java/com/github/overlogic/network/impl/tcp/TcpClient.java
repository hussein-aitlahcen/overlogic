package com.github.overlogic.network.impl.tcp;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import com.github.overlogic.network.Client;
import com.github.overlogic.network.ClientEvent;
import com.github.overlogic.network.ClientEventType;
import com.github.overlogic.network.DataChunk;
import com.github.overlogic.util.concurrent.actor.message.AbstractMessage;

public abstract class TcpClient<T extends TcpClient<T>> extends Client<TcpClient<T>> implements CompletionHandler<Integer, Void> {

	private final ByteBuffer buffer;
	private final AsynchronousSocketChannel socket;
	
	public TcpClient(final int identity, final ByteBuffer buffer, final AsynchronousSocketChannel socket) {
		super(identity);
		this.socket = socket;
		this.buffer = buffer;
	}
	
	public final ByteBuffer buffer() {
		return this.buffer;
	}
	
	public final AsynchronousSocketChannel socket() {
		return this.socket;
	}
	
	public void read() {
		this.buffer.position(0);
		this.socket.read(this.buffer, null, this);
	}
	
	@Override
	public void completed(final Integer bytesRead, final Void attachment) {
		if(bytesRead > 0) {
			final byte[] chunk = new byte[bytesRead];
			this.buffer.position(0);
			this.buffer.get(chunk);
			this.send(new DataChunk(ByteBuffer.wrap(chunk)));
			this.read();
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
		final AbstractMessage event = new ClientEvent<TcpClient<T>>(ClientEventType.DISCONNECTED, this);
		this.notifyObservers(observer -> {
			observer.send(event);
		});
		try {
			this.socket.close();
		} catch (IOException e) {
			
		}		
		this.kill();
	}
}
