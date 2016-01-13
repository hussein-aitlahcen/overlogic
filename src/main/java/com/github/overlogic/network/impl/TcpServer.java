package com.github.overlogic.network.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import com.github.overlogic.network.ClientEvent;
import com.github.overlogic.network.ClientEventType;
import com.github.overlogic.network.Server;

public abstract class TcpServer<T extends TcpClient<T>> extends Server<T> implements CompletionHandler<AsynchronousSocketChannel, ByteBuffer> {
	public static final int BUFFER_SIE = 1024;
	
	private final AsynchronousServerSocketChannel serverChannel;
	
	public TcpServer(final String host, final int port) throws IOException {
		super(host, port);
        this.serverChannel = AsynchronousServerSocketChannel.open();
	}
	
	private void beginAccept() {
		this.serverChannel.accept(ByteBuffer.allocate(BUFFER_SIE), this);
	}
		
	@Override
	public void completed(final AsynchronousSocketChannel channel, final ByteBuffer buffer) {
		System.out.println("server::completed accepted client");
		T client = this.createClient(buffer, channel);
		if(this.acceptable(client)) {
			client.observed(this);
			this.send(new ClientEvent(ClientEventType.CONNECTED, client));
			this.addChild(client);
		}
		else {
			try {
				channel.close();
			} 
			catch (IOException e) {				
			}
		}
		this.beginAccept();
	}

	@Override
	public void failed(final Throwable exc, final ByteBuffer attachment) {
		
	}  
	
	@Override
	public void listen() throws Exception {	
        if(!this.serverChannel.isOpen())
        	throw new SockListeningEx("Could not listen to address : " + host() + ":" + port());        	
        this.serverChannel.bind(new InetSocketAddress(host(), port()));
        this.beginAccept();
	}
	
	public abstract boolean acceptable(final T client);
	public abstract T createClient(final ByteBuffer buffer, final AsynchronousSocketChannel channel);
}
