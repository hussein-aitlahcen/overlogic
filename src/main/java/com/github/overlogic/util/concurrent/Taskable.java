package com.github.overlogic.util.concurrent;

public interface Taskable {
	void execute(final long delta) throws Exception;
}
