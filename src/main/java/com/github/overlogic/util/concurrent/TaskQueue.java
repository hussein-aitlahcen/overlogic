package com.github.overlogic.util.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.github.overlogic.util.TimeSource;
import com.github.overlogic.util.concurrent.actor.Actor;

public final class TaskQueue extends Actor {
	
	private static final int THREADPOOL_SIZE = Runtime.getRuntime().availableProcessors();
	private static final ScheduledExecutorService ThreadPool = Executors.newScheduledThreadPool(THREADPOOL_SIZE);
	
	private final String name;
	private final long interval;
	private final TimeSource timeSource;
	
	public TaskQueue(final String name, final long interval, final TimeSource timeSource) {
		this.name = name;
		this.interval = interval;
		this.timeSource = timeSource;
	}
	
	public final String name() {
		return this.name;
	}
	
	private final long realTime() {
		return this.timeSource.current();
	}
	
	public void start() {
		this.loop(0);
	}
	
	private void loop(final long milliseconds) {
		ThreadPool.schedule(() -> this.execute(), milliseconds, TimeUnit.MILLISECONDS);
	}
		
	public void execute() {
		
		long begin = this.realTime();
		long delta = begin - this.cumulatedTime;
		
		try {
			this.execute(delta);
		} 
		catch (Exception e) {
			// TODO: log
			System.out.println(e);
		}
		
		long end = this.realTime();
		long updateTime = end - begin;
		long delay = Math.max(0,  this.interval - updateTime);
		
		
		
		this.loop(delay);
	}
}
