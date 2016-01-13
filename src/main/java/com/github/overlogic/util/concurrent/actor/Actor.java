package com.github.overlogic.util.concurrent.actor;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.github.overlogic.util.Observed;
import com.github.overlogic.util.TypeSwitch;
import com.github.overlogic.util.concurrent.TemporalTask;

public abstract class Actor extends Observed<Actor> implements TemporalTask {
	
	protected final ArrayList<Actor> childs;
	protected final ConcurrentLinkedQueue<Message> messages;
	protected long cumulatedTime;
	protected boolean completed;
	
	public Actor() {
		this.childs = new ArrayList<Actor>();
		this.messages = new ConcurrentLinkedQueue<Message>();
		this.completed = false;
	}
	
	protected ArrayList<Actor> childs() {
		return this.childs;
	}
		
	public void addChild(final Actor child) {
		this.send(new AddChild(child));
	}
	
	public void removeChild(final Actor child) {
		this.send(new RemoveChild(child));
	}
	
	public void send(final Message message) {
		this.messages.offer(message);
	}	
		
	@Override
	public boolean completed() {
		return this.completed;
	}
	
	@Override
	public void execute(final long delta) throws Exception {
		this.accumulate(delta);
		int size = this.messages.size();
		for(int i = 0; i < size; i++) {
			this.handle(new TypeSwitch<Message>(this.messages.poll()));
		}		
		for(int i = this.childs.size() - 1; i > -1; i--) {
			Actor child = this.childs.get(i);
			child.execute(delta);
			if(child.completed()) {
				this.remove(child);
			}
		}
	}

	private void accumulate(final long delta) {
		this.cumulatedTime += delta;
	}
	
	private void add(final Actor child) {
		this.childs.add(child);
	}
	
	private void remove(final Actor child) {
		this.childs.remove(child);
	}
	
	private void handleAddChild(AddChild message) {
		this.add(message.child());
	}
	
	private void handleRemoveChild(RemoveChild message) {
		this.remove(message.child());
	}
	
	private void handleKill(Kill message) {
		completed = true;
	}
	
	public boolean handle(final TypeSwitch<Message> sw) {	
		return sw
				.with(AddChild.class, this::handleAddChild)
				.with(RemoveChild.class, this::handleRemoveChild)
				.with(Kill.class, this::handleKill)
				.handled();
	}
}
