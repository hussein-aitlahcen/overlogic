package com.github.overlogic.util;

public interface Identities<T> {	
	T acquire();
	void release(final T identity);
}
