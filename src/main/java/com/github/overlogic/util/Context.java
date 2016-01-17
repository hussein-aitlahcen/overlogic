package com.github.overlogic.util;

import com.github.overlogic.util.concurrent.actor.message.AbstractMessage;

public interface Context<T> {
	T tell(final AbstractMessage message);
}
