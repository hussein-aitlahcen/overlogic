package com.github.overlogic.util.concurrent.actor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.github.overlogic.util.TimeSource;

public final class TaskQueue extends Actor {
	
	private static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();
	 
	private volatile boolean running;
	private final String name;
	private final long interval;
	private final TimeSource timeSource;
		
	public TaskQueue(final String name, final long interval, final TimeSource timeSource) {
		this.name = name;
		this.interval = interval;
		this.timeSource = timeSource;
		this.running = false;
	}
	
	public boolean running() {
		return this.running;
	}
	
	public final String name() {
		return this.name;
	}
	
	private final long realTime() {
		return this.timeSource.current();
	}
	
	public void start() {
		this.running = true;
		THREAD_POOL.submit(() -> this.execute());
	}
	
	public void gracefulStop() {
		this.kill();
	}
	
	public void abruptStop() {
		this.running = false;
	}
	
	public synchronized void synchronize() throws InterruptedException {
		this.wait();
	}
	
	private void execute() {
		 				
		final long begin = this.realTime();	
		
		if(this.cumulatedTime == 0) {
			this.cumulatedTime = begin;
		}
		
		final long delta = begin - this.cumulatedTime;
		
		try {
			this.execute(delta);
		} 
		catch (Exception e) {
			LOGGER.error("Actor execution error {}", e.toString());
		}
		
		final long end = this.realTime();
		final long updateTime = end - begin;
		final long delay = Math.max(0,  this.interval - updateTime);
				
		try {
			TimeUnit.MILLISECONDS.sleep(delay);
		} 
		catch (InterruptedException e) {
			LOGGER.error("TaskQueue sleep interrupted {}", e.toString());
		}
		
		if(this.running) {
			THREAD_POOL.submit(() -> execute());
		}
		else {
			this.notify();
		}
	}
}
