package com.github.overlogic.util.concurrent;

public interface Service<T> extends Runnable, Startable<T>, Stoppable<T>, Synchronizable<T> {
	boolean running();
}
