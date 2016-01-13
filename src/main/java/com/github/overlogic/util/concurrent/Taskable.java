package com.github.overlogic.util.concurrent;

public interface Taskable {
	void execute(long delta) throws Exception;
}
