package com.github.overlogic.util;

public interface Identities<T> {	
	T give();
	void take(final T identity);
}
