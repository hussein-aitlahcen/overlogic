package com.github.overlogic.util;

import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class Observed<T> {
	private final ArrayList<T> observers;
	
	public Observed() {
		this.observers = new ArrayList<T>();
	}
	
	public void observed(final T observer) {
		synchronized(this.observers) {
			this.observers.add(observer);
		}
	}
	
	public void ignored(final T observer) {
		synchronized(this.observers) {
			this.observers.remove(observer);
		}
	}
	
	public void notifyObservers(final Consumer<T> fun) {
		synchronized(this.observers) {
			for(T observer : this.observers) {
				fun.accept(observer);
			}
		}
	}
}
