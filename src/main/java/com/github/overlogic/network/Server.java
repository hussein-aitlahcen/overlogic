package com.github.overlogic.network;

import com.github.overlogic.util.Configuration;
import com.github.overlogic.util.IntRangeIdentities;
import com.github.overlogic.util.TypeSwitch;
import com.github.overlogic.util.concurrent.actor.Actor;
import com.github.overlogic.util.concurrent.actor.message.AbstractMessage;

public abstract class Server<T extends Client<T>> extends Actor {

	public static final String HOST = "host";
	public static final String PORT = "port";
	public static final String MAX_CLIENTS = "max.clients";
	
	private final IntRangeIdentities clientIdentities;
	private final String host;
	private final int port;
	private final int maxClients;
	
	public Server(final Configuration configuration) throws Exception {
		this.host = configuration.value(HOST);
        this.port = Integer.valueOf(configuration.value(PORT));
        this.maxClients = Integer.valueOf(configuration.value(MAX_CLIENTS));
		this.clientIdentities = new IntRangeIdentities(0, this.maxClients() + 1);
	}
	
	protected final int acquireClientIdentity() {
		return this.clientIdentities.acquire();
	}
	
	protected final void releaseClientIdentity(final int identity) {
		this.clientIdentities.release(identity);
	}
	
	public final String host() {
		return this.host;
	}
	
	public final int port() {
		return this.port;
	}

	public final int maxClients() {
		return this.maxClients;
	}
	
	public final boolean canAcceptMoreClients() {
		return this.childs().size() < this.maxClients();
	}
	
	protected void handleClientEvent(final ClientEvent<T> event) {
		LOGGER.debug("Server client {}", event.type());		
		final T client = event.client();
		switch(event.type()) {
			case CONNECTED:
				this.clientConnected(client);
				break;
			case DISCONNECTED:
				this.clientDisconnected(client);
				break;			
		}		
		this.fireClientEvent(event);
	}
	
	private void clientConnected(final T client) {
		this.addChild(client);	
	}
	
	private void clientDisconnected(final T client) {
		this.releaseClientIdentity(client.identity());
	}
	
	private void fireClientEvent(final ClientEvent<T> event) {
		this.notifyObservers(event);
	}
	
	@Override
	public boolean handle(final TypeSwitch<AbstractMessage> sw) {
		return sw
				.with(ClientEvent.class, this::handleClientEvent)
				.handled() || super.handle(sw);		
	}
	
	abstract public void listen() throws Exception;
}
