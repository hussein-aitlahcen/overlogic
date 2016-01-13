package com.github.overlogic.network;

import com.github.overlogic.util.TypeSwitch;
import com.github.overlogic.util.concurrent.actor.Actor;
import com.github.overlogic.util.concurrent.actor.Message;

public abstract class Server<T extends Actor> extends Actor {
	private final String host;
	private final int port;
	
	public Server(final String host, final int port) {
		this.host = host;
		this.port = port;
	}
	
	public final String host() {
		return this.host; 
	}
	
	public final int port() {
		return this.port;
	}
	
	@SuppressWarnings("unchecked")
	private void handleClientEvent(ClientEvent event) {
		T client = (T)event.client();
		switch(event.type()) {
			case CONNECTED:
				this.addChild(client);
				break;
			case DISCONNECTED:
				this.removeChild(client);
				break;			
		}
	}
	
	@Override
	public boolean handle(TypeSwitch<Message> sw) {
		return sw
				.with(ClientEvent.class, this::handleClientEvent)
				.handled() || super.handle(sw);		
	}
	
	abstract public void listen() throws Exception;
}
