package com.github.overlogic.util;

import java.util.function.Consumer;

public final class TypeSwitch<T> {
	private final T object;
	private boolean handled;
	public TypeSwitch(final T object) {
		this.object = object;
		this.handled = false;
	}
	
	public final boolean handled() {
		return this.handled;
	}
	
	@SuppressWarnings("unchecked")
	public final <U extends T> TypeSwitch<T> with(final Class<U> clazz, final Consumer<U> action) {
		if(!this.handled) {
			if(clazz.isInstance(this.object)) {
				this.handled = true;
				action.accept((U)this.object);
			}
		}
		return this;
	}
}
