package com.github.overlogic.util.concurrent.actor;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.overlogic.util.Context;
import com.github.overlogic.util.Nameable;
import com.github.overlogic.util.TimeSource;
import com.github.overlogic.util.concurrent.Service;

public final class ActorService extends AbstractActor implements Nameable, Service, Context {

	protected static final Logger LOGGER = LoggerFactory.getLogger(ActorService.class);
	
	public static final long DEFAULT_INTERVAL = 10;

	
	private volatile boolean running;
	private final Thread thread;
	private final long interval;
	private final TimeSource timeSource;
		
	public ActorService(final String name) {
		this(name, DEFAULT_INTERVAL);
	}
	
	public ActorService(final String name, final long interval) {
		this(name, interval, TimeSource.SYSTEM);
	}
	
	public ActorService(final String name, final long interval, final TimeSource timeSource) {
		this.interval = interval;
		this.timeSource = timeSource;
		this.running = false;
		this.thread = new Thread(this, name);
	}
	
	@Override
	public boolean running() {
		return this.running;
	}
	
	@Override
	public final String name() {
		return this.thread.getName();
	}
	
	@Override		
	public void start() {
		this.running = true;
		this.thread.start();
	}
	
	@Override
	public void stop() {
		this.running = false;
	}
	
	@Override
	public void synchronize() throws Exception {
		this.thread.join();
	}
	
	@Override
	public void run() {	
		
		while(this.running) {		
			
			final long begin = this.realTime();	
			
			this.initializeCumulatedTimeIfFirstLoop(begin);
			
			final long delta = begin - this.cumulatedTime();
							
			try {
				this.execute(delta);
			} catch (Exception e) {
				LOGGER.error("ActorService {} failed to process messages and childs {}", this.name(), e.toString());
			}
			
			final long end = this.realTime();
			final long updateTime = end - begin;
			final long delay = Math.max(0,  this.interval - updateTime);
			
			try {
				this.synchronizeDelay(delay);
			} 
			catch (Exception e) {
				LOGGER.error("ActorService {} sleep interrupted {}", this.name(), e.toString());
			}
			
		}
		
	}
	
	private void initializeCumulatedTimeIfFirstLoop(final long time) {
		if(this.cumulatedTime() == 0) {
			this.accumulate(time);
		}
	}
		
	private void synchronizeDelay(final long delay) throws Exception {
		TimeUnit.MILLISECONDS.sleep(delay);
	}
	
	private final long realTime() {
		return this.timeSource.current();
	}	
}
