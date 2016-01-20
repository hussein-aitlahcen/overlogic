package com.github.overlogic.network;

import com.github.overlogic.network.message.ClientEvent;
import com.github.overlogic.util.Configuration;
import com.github.overlogic.util.Identities;
import com.github.overlogic.util.IntRangeIdentities;
import com.github.overlogic.util.TypeSwitch;
import com.github.overlogic.util.concurrent.actor.AbstractActor;
import com.github.overlogic.util.concurrent.actor.message.AbstractMessage;
import com.github.overlogic.util.concurrent.actor.message.AddChild;

public abstract class AbstractServer<T extends AbstractClient<T>> extends AbstractActor {
	
	public static final String HOST = "host";
	public static final String PORT = "port";
	public static final String MAX_CLIENTS = "maxClients";
	
	private final String host;
	private final int port;
	private final int maxClients;
	private final Identities<Integer> clientIdentities;
	
	public AbstractServer(final Configuration configuration) throws Exception {
		this.host = configuration.value(HOST);
        this.port = Integer.valueOf(configuration.value(PORT));
        this.maxClients = Integer.valueOf(configuration.value(MAX_CLIENTS));
		this.clientIdentities = new IntRangeIdentities(0, this.maxClients() + 1);
	}
	
	protected final int clientIdentityAcquire() {
		return this.clientIdentities.give();
	}
	
	protected final void clientIdentityRelease(final int identity) {
		this.clientIdentities.take(identity);
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
		return super.childs().size() < this.maxClients();
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
		this.tell(new AddChild(client));	
	}
	
	private void clientDisconnected(final T client) {
		this.clientIdentityRelease(client.identity());
	}
	
	protected void fireClientEvent(final ClientEvent<T> event) {
		this.tellObservers(event);
	}
	
	@Override
	public boolean handle(final TypeSwitch<AbstractMessage> sw) throws Exception {
		return sw
				.with(ClientEvent.class, this::handleClientEvent)
				.handled() || super.handle(sw);		
	}
	
	abstract public void listen() throws Exception;
}
