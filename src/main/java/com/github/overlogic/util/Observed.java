package com.github.overlogic.util;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public abstract class Observed<T> implements Observable<T> {
	private final ConcurrentLinkedQueue<T> observers;
	
	public Observed() {
		this.observers = new ConcurrentLinkedQueue<T>();
	}
	
	@Override
	public void observedBy(final T observer) {
		this.observers.offer(observer);
	}

	@Override
	public void notObservedAnymoreBy(final T observer) {
		this.observers.remove(observer);		
	}

	@Override
	public void notifyObservers(final Consumer<T> fun) {
		for(T observer : this.observers) {
			fun.accept(observer);
		}
	}
}
