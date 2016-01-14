package com.github.overlogic.util;

import java.util.List;
import java.util.Stack;

public abstract class GeneratedIdentities<T> implements Identities<T> {
	
	private final Stack<T> identities;

	public GeneratedIdentities(final List<T> identities) {
		this.identities = new Stack<>();
		this.identities.addAll(identities);
	}
	
	public T acquire() {
		return this.identities.pop();
	}
	
	public void release(final T identity) {
		this.identities.push(identity);
	}
}
