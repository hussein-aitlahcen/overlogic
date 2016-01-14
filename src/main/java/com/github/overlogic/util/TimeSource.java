package com.github.overlogic.util;

public interface TimeSource {
	public static final TimeSource SYSTEM = new TimeSource() {
		@Override
		public long current() {
			return System.currentTimeMillis();
		}		
	};
	long current();
}
