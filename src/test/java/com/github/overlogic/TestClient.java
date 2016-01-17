package com.github.overlogic;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;

import com.github.overlogic.network.message.DataChunk;
import com.github.overlogic.network.tcp.AbstractTcpClient;
import com.github.overlogic.util.TypeSwitch;
import com.github.overlogic.util.concurrent.actor.message.AbstractMessage;

public final class TestClient extends AbstractTcpClient<TestClient> {
	public TestClient(int identity, ByteBuffer buffer, AsynchronousSocketChannel socket) {
		super(identity, buffer, socket);
	}
	
	@Override
	public boolean handle(final TypeSwitch<AbstractMessage> sw) {
		return sw
				.with(DataChunk.class, (message) -> {
					LOGGER.debug(
							String.format(
									"received %s", 
									Charset
									.defaultCharset()
									.decode(message.chunk())
									.toString()
							)
					);
				}).handled() || super.handle(sw);
	}
}
