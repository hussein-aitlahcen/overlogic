package com.github.overlogic.network.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import com.github.overlogic.network.AbstractServer;
import com.github.overlogic.network.message.ClientEvent;
import com.github.overlogic.network.message.ClientEventType;
import com.github.overlogic.network.tcp.message.AcceptSocket;
import com.github.overlogic.util.Configuration;
import com.github.overlogic.util.LinearBufferStack;
import com.github.overlogic.util.TypeSwitch;
import com.github.overlogic.util.concurrent.actor.message.AbstractMessage;

public abstract class AbstractTcpServer<T extends AbstractTcpClient<T>> extends AbstractServer<AbstractTcpClient<T>> implements CompletionHandler<AsynchronousSocketChannel, Void> {
	
	public static final String BUFFER_SIZE = "tcp.bufferSize";
	
	private final AsynchronousServerSocketChannel serverChannel;
	private final LinearBufferStack bufferStack;
	private final int bufferSize;
	
	public AbstractTcpServer(final Configuration configuration) throws Exception {
		super(configuration);
		this.bufferSize = Integer.valueOf(configuration.value(BUFFER_SIZE));
		this.serverChannel = AsynchronousServerSocketChannel.open();
		this.bufferStack = new LinearBufferStack(this.bufferSize, super.maxClients() + 1);
	}
		
	public final int bufferSize() {
		return this.bufferSize;
	}
	
	private ByteBuffer clientBufferAcquire(final int identity) {
		return ByteBuffer.wrap(
					this.bufferStack.buffer(), 
					identity * this.bufferSize, 
					this.bufferSize
				);
	}
		
	private void acceptNext() {
		this.serverChannel.accept(null, this);
	}
			
	private void clientAccepted(final T client) {
		client.read();
		client.observedBy(this);
		super.handleClientEvent(
				new ClientEvent<AbstractTcpClient<T>>(
						ClientEventType.CONNECTED, 
						client
				)
		);
	}
	
	private void clientRefused(final T client) {
		LOGGER.debug("TcpServer refused client");
		this.clientIdentityRelease(client.identity());
		try {
			client.socket().close();
		} 
		catch (IOException e) {	
			LOGGER.debug(e.toString());
		}
	}
	
	private void handleAcceptSocket(final AcceptSocket message) {
		final int identity = this.clientIdentityAcquire();
		final ByteBuffer buffer = this.clientBufferAcquire(identity);
		final T client = this.createClient(identity, buffer, message.socket());
		if(this.acceptable(client)) {
			this.clientAccepted(client);
		}
		else {
			this.clientRefused(client);
		}
	}
	
	@Override
	public boolean handle(final TypeSwitch<AbstractMessage> sw) {
		return sw
				.with(AcceptSocket.class, this::handleAcceptSocket)
				.handled() || super.handle(sw);
	}
	
	@Override
	public void completed(final AsynchronousSocketChannel channel, final Void attachment) {		
		super.tell(new AcceptSocket(channel));
		this.acceptNext();
	}

	@Override
	public void failed(final Throwable exc, final Void attachment) {
		this.acceptNext();
	}  
	
	@Override
	public void listen() throws Exception {	
        if(!this.serverChannel.isOpen())
        	throw new Exception("TcpServer failed to open socket channel : " + host() + ":" + port());        	
		this.serverChannel.bind(new InetSocketAddress(host(), port()));
		this.acceptNext();
		LOGGER.debug("TcpServer listening");
	}
	
	public boolean acceptable(final T client) {
		return super.canAcceptMoreClients();
	}
	
	public abstract T createClient(final int identity, final ByteBuffer buffer, final AsynchronousSocketChannel socket);

}
