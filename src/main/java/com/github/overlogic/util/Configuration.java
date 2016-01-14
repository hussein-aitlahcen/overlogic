package com.github.overlogic.util;

public interface Configuration {
	String value(final String key) throws ConfigKeyNotFoundException;
	void value(final String key, final String value);
}
