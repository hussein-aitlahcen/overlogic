package com.github.overlogic.network;

import com.github.overlogic.util.ConfigKeyNotFoundException;
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
	
	public Server(final Configuration configuration) throws ConfigKeyNotFoundException {
		this.host = configuration.value(HOST);
        this.port = Integer.valueOf(configuration.value(PORT));
        this.maxClients = Integer.valueOf(configuration.value(MAX_CLIENTS));
		this.clientIdentities = new IntRangeIdentities(0, this.maxClients());
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

	// + 1 because when we have reached the maximum, the next client should be kicked
	// Since we assign its identity before kicking it, we should ensure that there 
	// is an identity available
	public final int maxClients() {
		return this.maxClients + 1;
	}
	
	public final boolean canAcceptMoreClients() {
		return this.childs().size() < this.maxClients() - 1;
	}
	
	protected void handleClientEvent(final ClientEvent<T> event) {
		LOGGER.debug("Server client {}", event.type());		
		final T client = event.client();
		switch(event.type()) {
			case CONNECTED:
				this.add(client);
				break;
			case DISCONNECTED:
				this.remove(client);
				this.releaseClientIdentity(client.identity());
				break;			
		}
		this.notifyObservers(observer -> {
			observer.send(event);
		});
	}

	
	@Override
	public boolean handle(final TypeSwitch<AbstractMessage> sw) {
		return sw
				.with(ClientEvent.class, this::handleClientEvent)
				.handled() || super.handle(sw);		
	}
	
	abstract public void listen() throws Exception;
}
