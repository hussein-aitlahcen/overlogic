package com.github.overlogic.util;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public interface Observable<T> {
	
	public ConcurrentLinkedQueue<T> observers();
	
	public default void observedBy(final T observer) {
		this.observers().offer(observer);
	}

	public default void notObservedAnymoreBy(final T observer) {
		this.observers().remove(observer);		
	}

	public default void notifyObservers(final Consumer<T> fun) {
		for(T observer : this.observers()) {
			fun.accept(observer);
		}
	}
}
