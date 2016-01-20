package com.github.overlogic.network.tcp;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import com.github.overlogic.network.AbstractClient;
import com.github.overlogic.network.message.ClientEvent;
import com.github.overlogic.network.message.ClientEventType;
import com.github.overlogic.network.message.DataChunk;

public abstract class AbstractTcpClient<T extends AbstractTcpClient<T>> extends AbstractClient<AbstractTcpClient<T>> implements CompletionHandler<Integer, Void> {

	private final ByteBuffer buffer;
	private final AsynchronousSocketChannel socket;
	private final String ip;
	
	public AbstractTcpClient(final int identity, final ByteBuffer buffer, final AsynchronousSocketChannel socket) throws Exception {
		super(identity);
		this.socket = socket;
		this.buffer = buffer;
		this.ip = this.socket.getRemoteAddress().toString();
	}
	
	public final String ip() {
		return this.ip;
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
			this.tell(new DataChunk(ByteBuffer.wrap(chunk)));
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
	
	private void closeSocket() {
		try {
			this.socket.shutdownInput();
			this.socket.shutdownOutput();
			this.socket.close();
		} catch (IOException e) {			
		}		
	}
	
	private void disconnected() {
		this.closeSocket();
		this.tellObservers(
				new ClientEvent<AbstractTcpClient<T>>(
						ClientEventType.DISCONNECTED, 
						this
				)
		);
		this.kill();
	}
	
}
