package com.github.overlogic.network.impl.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import com.github.overlogic.network.ClientEvent;
import com.github.overlogic.network.ClientEventType;
import com.github.overlogic.network.Server;
import com.github.overlogic.util.ConfigKeyNotFoundException;
import com.github.overlogic.util.Configuration;
import com.github.overlogic.util.TypeSwitch;
import com.github.overlogic.util.concurrent.actor.message.AbstractMessage;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

public abstract class TcpServer<T extends TcpClient<T>> extends Server<TcpClient<T>> implements CompletionHandler<AsynchronousSocketChannel, Void> {
	public static final String BUFFER_SIZE = "tcp.bufferSize";
	
	private final AsynchronousServerSocketChannel serverChannel;
	private final Supplier<byte[]> bufferCache;
	private final int bufferSize;
	
	public TcpServer(final Configuration configuration) throws IOException, ConfigKeyNotFoundException {
		super(configuration);
		this.serverChannel = AsynchronousServerSocketChannel.open();
		this.bufferCache = Suppliers.memoize(this::allocate);
		this.bufferSize = Integer.valueOf(configuration.value(BUFFER_SIZE));
	}
	
	private byte[] allocate() {
		return new byte[this.bufferSize() * this.maxClients()];		
	}
		
	public final byte[] buffer() {
		return this.bufferCache.get();
	}

	public final int bufferSize() {
		return this.bufferSize;
	}
	
	private ByteBuffer acquireClientBuffer(final int identity) {
		return ByteBuffer.wrap(
					this.buffer(), 
					identity * this.bufferSize, 
					this.bufferSize
				);
	}
		
	private void acceptNext() {
		this.serverChannel.accept(null, this);
	}
			
	private void acceptClient(final T client) {
		client.read();
		client.observedBy(this);
		this.handleClientEvent(new ClientEvent<TcpClient<T>>(ClientEventType.CONNECTED, client));
	}
	
	private void refuseClient(final T client) {
		LOGGER.debug("TcpServer refused client");
		try {
			client.socket().close();
			this.releaseClientIdentity(client.identity());
		} 
		catch (IOException e) {	
			System.out.println(e);
		}
	}
	
	private void acceptSocket(final AcceptSocket message) {
		final int identity = this.acquireClientIdentity();
		final ByteBuffer buffer = this.acquireClientBuffer(identity);
		final T client = this.createClient(identity, buffer, message.socket());
		if(this.acceptable(client)) {
			this.acceptClient(client);
		}
		else {
			this.refuseClient(client);
		}
	}
	
	@Override
	public boolean handle(final TypeSwitch<AbstractMessage> sw) {
		return sw
				.with(AcceptSocket.class, this::acceptSocket)
				.handled() || super.handle(sw);
	}
	
	@Override
	public void completed(final AsynchronousSocketChannel channel, final Void attachment) {		
		this.send(new AcceptSocket(channel));
		this.acceptNext();
	}

	@Override
	public void failed(final Throwable exc, final Void attachment) {
		this.acceptNext();
	}  
	
	@Override
	public void listen() throws Exception {	
        if(!this.serverChannel.isOpen())
        	throw new SocketListeningException("Could not listen to address : " + host() + ":" + port());        	
		this.serverChannel.bind(new InetSocketAddress(host(), port()));
		this.acceptNext();
		LOGGER.debug("TcpServer listening");
	}
	
	public boolean acceptable(final T client) {
		return this.canAcceptMoreClients();
	}
	public abstract T createClient(final int identity, final ByteBuffer buffer, final AsynchronousSocketChannel socket);
}
