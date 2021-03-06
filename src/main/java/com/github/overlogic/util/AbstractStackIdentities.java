package com.github.overlogic.util;

import java.util.List;
import java.util.Stack;

public abstract class AbstractStackIdentities<T> implements Identities<T> {
	
	private final Stack<T> identities;

	public AbstractStackIdentities(final List<T> identities) {
		this.identities = new Stack<>();
		this.identities.addAll(identities);
	}
	
	@Override
	public T give() {
		return this.identities.pop();
	}

	@Override
	public void take(final T identity) {
		this.identities.push(identity);
	}
	
}
