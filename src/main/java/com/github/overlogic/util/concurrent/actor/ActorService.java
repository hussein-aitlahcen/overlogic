package com.github.overlogic.util.concurrent.actor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.github.overlogic.util.Nameable;
import com.github.overlogic.util.TimeSource;
import com.github.overlogic.util.concurrent.Service;
import com.github.overlogic.util.concurrent.actor.message.AbstractMessage;

public final class ActorService extends AbstractActor implements Nameable, Service<ActorService> {
	
	public static final long DEFAULT_INTERVAL = 10;
	
	private static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();
	 
	private volatile boolean running;
	private final String name;
	private final long interval;
	private final TimeSource timeSource;
	private long cumulatedTime;
		
	public ActorService(final String name) {
		this(name, DEFAULT_INTERVAL);
	}
	
	public ActorService(final String name, final long interval) {
		this(name, interval, TimeSource.SYSTEM);
	}
	
	public ActorService(final String name, final long interval, final TimeSource timeSource) {
		this.name = name;
		this.interval = interval;
		this.timeSource = timeSource;
		this.cumulatedTime = 0;
		this.running = false;
	}
	
	@Override
	public boolean running() {
		return this.running;
	}
	
	@Override
	public final String name() {
		return this.name;
	}
		
	@Override
	public ActorService start() {
		this.running = true;
		THREAD_POOL.submit(this);
		return this;
	}
	
	@Override
	public ActorService stop() {
		this.running = false;
		return this;
	}
		
	@Override
	public ActorService synchronize() throws Exception {
		synchronized(this) {
			this.wait();
		}
		return this;
	}
	
	@Override
	public ActorService tell(final AbstractMessage message) {
		super.tell(message);
		return this;
	}
	
	@Override
	public void run() {		 				
		final long begin = this.realTime();	
		
		this.initializeCumulatedTimeIfFirstLoop(begin);
		
		final long delta = begin - this.cumulatedTime;
				
		try {
			this.processMessagesAndUpdateChilds(delta);
		} catch (Exception e) {
			LOGGER.error("ActorService {} failed to process messages and childs {}", this.name, e.toString());
		}
		
		final long end = this.realTime();
		final long updateTime = end - begin;
		final long delay = Math.max(0,  this.interval - updateTime);
		
		try {
			this.synchronizeDelay(delay);
		} 
		catch (Exception e) {
			LOGGER.error("ActorService {} sleep interrupted {}", this.name, e.toString());
		}
		
		this.loopIfStillRunning();
	}
	
	private void initializeCumulatedTimeIfFirstLoop(final long time) {
		if(this.cumulatedTime == 0) {
			this.cumulatedTime = time;
		}
	}
	
	private void loopIfStillRunning() {
		if(this.running) {
			THREAD_POOL.submit(this);
		}
		else {
			this.fireStop();
		}
	}
	
	private void processMessagesAndUpdateChilds(final long delta) throws Exception {
		super.update(delta);
	}
	
	private void synchronizeDelay(final long delay) throws Exception {
		TimeUnit.MILLISECONDS.sleep(delay);
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
