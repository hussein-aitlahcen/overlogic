package com.github.overlogic.util;

import java.util.function.Consumer;

public interface Observable<T> {
	public void observed(final T observer);	
	public void ignored(final T observer);	
	public void notifyObservers(final Consumer<T> fun);
}
