package com.github.overlogic.util;

import java.util.function.UnaryOperator;

public interface Observable<T> {
	public void observe(final T observer);	
	public void ignore(final T observer);	
	public void notifyObservers(final UnaryOperator<T> fun);
}
