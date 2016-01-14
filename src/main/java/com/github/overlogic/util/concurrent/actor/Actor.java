package com.github.overlogic.util.concurrent.actor;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.overlogic.util.Observable;
import com.github.overlogic.util.TypeSwitch;
import com.github.overlogic.util.concurrent.ExpirableTask;
import com.github.overlogic.util.concurrent.actor.message.AbstractMessage;
import com.github.overlogic.util.concurrent.actor.message.AddChild;
import com.github.overlogic.util.concurrent.actor.message.Kill;
import com.github.overlogic.util.concurrent.actor.message.RemoveChild;

public abstract class Actor implements Observable<Actor>, ExpirableTask {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(Actor.class);
	
	private final ConcurrentLinkedQueue<Actor> observers;
	protected final ArrayList<Actor> childs;
	protected final ConcurrentLinkedQueue<AbstractMessage> messages;
	protected long cumulatedTime;
	protected boolean completed;
	
	public Actor() {
		this.observers = new ConcurrentLinkedQueue<Actor>();
		this.childs = new ArrayList<Actor>();
		this.messages = new ConcurrentLinkedQueue<AbstractMessage>();
		this.completed = false;
	}

	protected ArrayList<Actor> childs() {
		return this.childs;
	}
	
	@Override
	public ConcurrentLinkedQueue<Actor> observers() {
		return this.observers;
	}
			
	public Actor addChild(final Actor child) {
		return this.send(new AddChild(child));
	}
	
	public Actor removeChild(final Actor child) {
		return this.send(new RemoveChild(child));
	}
	
	public void kill() {
		this.send(Kill.INSTANCE);
	}
	
	public Actor send(final AbstractMessage message) {
		this.messages.offer(message);
		return this;
	}	
		
	@Override
	public boolean expired() {
		return this.completed;
	}
	
	@Override
	public void execute(final long delta) throws Exception {
		this.accumulate(delta);
		int size = this.messages.size();
		for(int i = 0; i < size; i++) {
			final AbstractMessage message = this.messages.poll();
			if(!this.handle(new TypeSwitch<AbstractMessage>(message))) {
				LOGGER.debug(
						"Actor unhandled message {}", 
						getClass().getSimpleName(), 
						message.getClass().getSimpleName());
			}
		}		
		for(int i = this.childs.size() - 1; i > -1; i--) {
			Actor child = this.childs.get(i);
			child.execute(delta);
			if(child.expired()) {
				this.remove(child);
				LOGGER.debug("Actor child completed");
			}
		}
	}

	private void accumulate(final long delta) {
		this.cumulatedTime += delta;
	}
	
	protected void add(final Actor child) {
		this.childs.add(child);
	}
	
	protected void remove(final Actor child) {
		this.childs.remove(child);
	}
	
	private void handleAddChild(final AddChild message) {
		this.add(message.child());
	}
	
	private void handleRemoveChild(final RemoveChild message) {
		this.remove(message.child());
	}
	
	private void handleKill(Kill message) {
		completed = true;
		LOGGER.debug("Actor killed");
	}
	
	public boolean handle(final TypeSwitch<AbstractMessage> sw) {	
		return sw
				.with(AddChild.class, this::handleAddChild)
				.with(RemoveChild.class, this::handleRemoveChild)
				.with(Kill.class, this::handleKill)
				.handled();
	}
}
