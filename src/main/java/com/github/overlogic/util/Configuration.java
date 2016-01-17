package com.github.overlogic.util;

public interface Configuration {
	String value(final String key) throws Exception;
	Configuration value(final String key, final String value);
}
