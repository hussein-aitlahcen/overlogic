package com.github.overlogic.util;

import java.util.function.Consumer;

public interface Observable<T> {		
	public void observedBy(final T observer);
	public void notObservedAnymoreBy(final T observer);
	public void notifyObservers(final Consumer<T> fun);
}
