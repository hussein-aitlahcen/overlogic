package com.github.overlogic.util;

public final class ConfigKeyNotFoundException extends Exception {
	public ConfigKeyNotFoundException(final String key) {
		super("Config value not found for key {" + key + "}");
	}
}
