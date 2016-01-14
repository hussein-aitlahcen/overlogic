package com.github.overlogic.util.concurrent.actor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.github.overlogic.util.Nameable;
import com.github.overlogic.util.TimeSource;
import com.github.overlogic.util.concurrent.Startable;
import com.github.overlogic.util.concurrent.Stoppable;
import com.github.overlogic.util.concurrent.Synchronizable;

public final class TaskQueue extends Actor implements Nameable, Runnable, Synchronizable, Stoppable, Startable {
	
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
	
	public TaskQueue doStart() {
		this.start();
		return this;
	}
	
	public TaskQueue doStop() {
		this.stop();
		return this;
	}
	
	public TaskQueue doSynchronize() throws InterruptedException {
		this.synchronize();
		return this;
	}
	
	public boolean running() {
		return this.running;
	}
	
	@Override
	public final String name() {
		return this.name;
	}
		
	@Override
	public void start() {
		this.running = true;
		THREAD_POOL.submit(this);
	}
	
	@Override
	public void stop() {
		this.running = false;
	}
		
	@Override
	public void synchronize() throws InterruptedException {
		synchronized(this) {
			this.wait();
		}
	}
	
	@Override
	public void run() {		 				
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
			THREAD_POOL.submit(this);
		}
		else {
			this.fireStop();
		}
	}

	private final long realTime() {
		return this.timeSource.current();
	}
	
	private void fireStop() {
		synchronized(this) {
			this.notifyAll();
		}
	}
}
