package com.github.overlogic.util;

public interface TimeSource {
	public static final TimeSource SYSTEM = () -> System.currentTimeMillis();	
	long current();
}
