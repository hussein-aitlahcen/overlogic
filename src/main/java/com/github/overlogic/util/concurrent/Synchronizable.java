package com.github.overlogic.util.concurrent;

public interface Synchronizable<T> {
	T synchronize() throws Exception;
}
