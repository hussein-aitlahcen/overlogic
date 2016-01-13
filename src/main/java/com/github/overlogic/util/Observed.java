package com.github.overlogic.util;

import java.util.ArrayList;
import java.util.function.UnaryOperator;

public abstract class Observed<T> {
	private final ArrayList<T> observers;
	
	public Observed() {
		this.observers = new ArrayList<T>();
	}
	
	public void observe(final T observer) {
		this.observers.add(observer);
	}
	
	public void ignore(final T observer) {
		this.observers.remove(observer);
	}
	
	public void notifyObservers(final UnaryOperator<T> fun) {
		for(T observer : this.observers) {
			fun.apply(observer);
		}
	}
}
