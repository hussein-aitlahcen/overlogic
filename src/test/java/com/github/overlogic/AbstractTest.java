package com.github.overlogic;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.log4j.PatternLayout;
import org.junit.Before;

public abstract class AbstractTest {

	protected static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AbstractTest.class);
	
	@Before
	public final void setup() {
		final ConsoleAppender console = new ConsoleAppender();
		final String PATTERN = "%d [%p|%C{1}] %m%n";
		console.setLayout(new PatternLayout(PATTERN));
		console.setThreshold(Level.DEBUG);
		console.activateOptions();
		Logger.getRootLogger().addAppender(console);
	}
}
