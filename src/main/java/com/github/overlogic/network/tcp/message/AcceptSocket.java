package com.github.overlogic.network.tcp.message;

import java.nio.channels.AsynchronousSocketChannel;

import com.github.overlogic.util.concurrent.actor.message.AbstractMessage;

public final class AcceptSocket extends AbstractMessage {
	
	private final AsynchronousSocketChannel socket;
	
	public AcceptSocket(final AsynchronousSocketChannel socket) {
		this.socket = socket;
	}
	
	public AsynchronousSocketChannel socket() {
		return this.socket;
	}
	
}
