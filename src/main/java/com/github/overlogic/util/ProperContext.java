package com.github.overlogic.util;

import com.github.overlogic.util.concurrent.actor.message.AbstractMessage;

public interface ProperContext {
	void tell(final AbstractMessage message);
}
