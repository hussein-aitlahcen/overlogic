package com.github.overlogic.util.concurrent.actor;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.overlogic.util.Observable;
import com.github.overlogic.util.TypeSwitch;
import com.github.overlogic.util.concurrent.ExpirableTask;
import com.github.overlogic.util.concurrent.ExpirableUpdatable;
import com.github.overlogic.util.concurrent.actor.message.AbstractMessage;
import com.github.overlogic.util.concurrent.actor.message.AddChild;
import com.github.overlogic.util.concurrent.actor.message.Kill;

public abstract class Actor implements Observable<Actor>, ExpirableUpdatable {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(Actor.class);
	
	private final ConcurrentLinkedQueue<Actor> observers;
	protected final ArrayList<ExpirableTask> childs;
	protected final ConcurrentLinkedQueue<AbstractMessage> messages;
	private boolean expired;
	
	public Actor() {
		this.observers = new ConcurrentLinkedQueue<Actor>();
		this.childs = new ArrayList<ExpirableTask>();
		this.messages = new ConcurrentLinkedQueue<AbstractMessage>();
		this.expired = false;
	}

	protected ArrayList<ExpirableTask> childs() {
		return this.childs;
	}
	
	@Override
	public ConcurrentLinkedQueue<Actor> observers() {
		return this.observers;
	}
			
	public Actor addChild(final Actor child) {
		return this.send(new AddChild(child));
	}
	
	public void kill() {
		this.send(Kill.INSTANCE);
	}
	
	public Actor send(final AbstractMessage message) {
		this.messages.offer(message);
		return this;
	}	
	
	protected void notifyObservers(final AbstractMessage message) {
		this.notifyObservers(observer -> {
			observer.send(message);
		});
	}
	
	private void handlePendingMessages() {
		int size = this.messages.size();
		for(int i = 0; i < size; i++) {
			final AbstractMessage message = this.messages.poll();
			if(!this.handle(new TypeSwitch<AbstractMessage>(message))) {
				LOGGER.debug(
						"Actor unhandled message {}", 
						getClass().getSimpleName(), 
						message.getClass().getSimpleName()
				);
			}
		}		
	}
	
	private void updateChildTasks(final long delta) throws Exception {
		for(int i = this.childs.size() - 1; i > -1; i--) {
			ExpirableTask child = this.childs.get(i);
			child.execute(delta);
			if(child.expired()) {
				this.remove(child);
				LOGGER.debug("Actor child completed");
			}
		}
	}
		
	@Override
	public boolean expired() {
		return this.expired;
	}
	
	protected void expired(boolean value) {
		this.expired = value;
	}
	
	@Override
	public void update(final long delta) throws Exception {
		this.handlePendingMessages();
		this.updateChildTasks(delta);
	}
	
	protected void add(final ExpirableTask child) {
		this.childs.add(child);
	}
	
	protected void remove(final ExpirableTask child) {
		this.childs.remove(child);
	}
	
	private void handleAddChild(final AddChild message) {
		this.add(new UpdateExpirableTask(message.child()));
	}
	
	private void handleKill(Kill message) {
		this.expired(true);
		LOGGER.debug("Actor killed");
	}
	
	public boolean handle(final TypeSwitch<AbstractMessage> sw) {	
		return sw
				.with(AddChild.class, this::handleAddChild)
				.with(Kill.class, this::handleKill)
				.handled();
	}
}
