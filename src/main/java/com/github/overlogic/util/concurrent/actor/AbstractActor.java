package com.github.overlogic.util.concurrent.actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.overlogic.util.Observable;
import com.github.overlogic.util.Context;
import com.github.overlogic.util.TypeSwitch;
import com.github.overlogic.util.concurrent.ExpirableTask;
import com.github.overlogic.util.concurrent.ExpirableUpdatable;
import com.github.overlogic.util.concurrent.actor.message.AbstractMessage;
import com.github.overlogic.util.concurrent.actor.message.AddChild;
import com.github.overlogic.util.concurrent.actor.message.AddObserver;
import com.github.overlogic.util.concurrent.actor.message.Kill;
import com.github.overlogic.util.concurrent.actor.message.NotifyObservers;
import com.github.overlogic.util.concurrent.actor.message.RemoveObserver;

public abstract class AbstractActor implements Observable<AbstractActor>, ExpirableUpdatable, Context<AbstractActor> {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractActor.class);
	
	private final List<AbstractActor> observers;
	protected final List<ExpirableTask> childs;
	protected final Queue<AbstractMessage> messages;
	private boolean expired;
	
	public AbstractActor() {
		this.observers = new ArrayList<AbstractActor>();
		this.childs = new ArrayList<ExpirableTask>();
		this.messages = new ConcurrentLinkedQueue<AbstractMessage>();
		this.expired = false;
	}
	
	@Override
	public void observedBy(final AbstractActor observer) {
		this.tell(new AddObserver(observer));
	}
	
	@Override
	public void notObservedAnymoreBy(final AbstractActor observer) {
		this.tell(new RemoveObserver(observer));
	}
	
	@Override
	public void notifyObservers(final Consumer<AbstractActor> action) {
		this.tell(new NotifyObservers(action));
	}

	protected List<ExpirableTask> childs() {
		return this.childs;
	}
	
	public void addChild(final AbstractActor child) {
		this.tell(new AddChild(child));
	}
				
	public void kill() {
		this.tell(Kill.INSTANCE);
	}
	
	public AbstractActor tell(final AbstractMessage message) {
		this.messages.offer(message);
		return this;
	}	
	
	protected void dispatchMessageToObservers(final AbstractMessage message) {
		this.notifyObservers(observer -> {
			observer.tell(message);
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
			}
		}
	}
	
	@Override
	public void update(final long delta) throws Exception {
		this.handlePendingMessages();
		this.updateChildTasks(delta);
	}
		
	@Override
	public boolean expired() {
		return this.expired;
	}
	
	protected void expired(boolean value) {
		this.expired = value;
	}
		
	protected void add(final ExpirableTask child) {
		this.childs.add(child);
	}
	
	protected void remove(final ExpirableTask child) {
		this.childs.remove(child);
	}
	
	private void handleAddChild(final AddChild message) {
		this.add(new UpdateExpirableObjectTask(message.child()));
	}
	
	private void handleKill(final Kill message) {
		this.expired(true);
	}
		
	private void handleAddObserver(final AddObserver message) {
		this.observers.add(message.observer());
	}
	
	private void handleRemoveObserver(final RemoveObserver message) {
		this.observers.remove(message.observer());
	}
	
	private void handleNotifyObservers(final NotifyObservers message) {
		this.observers.forEach(message.action());
	}
	
	public boolean handle(final TypeSwitch<AbstractMessage> sw) {	
		return sw
				.with(AddObserver.class, this::handleAddObserver)
				.with(RemoveObserver.class, this::handleRemoveObserver)
				.with(NotifyObservers.class, this::handleNotifyObservers)
				.with(AddChild.class, this::handleAddChild)
				.with(Kill.class, this::handleKill)
				.handled();
	}
	
}
