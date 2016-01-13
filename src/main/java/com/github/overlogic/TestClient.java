package com.github.overlogic;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;

import com.github.overlogic.network.impl.DataChunk;
import com.github.overlogic.network.impl.TcpClient;
import com.github.overlogic.util.TypeSwitch;
import com.github.overlogic.util.concurrent.actor.Message;

public final class TestClient extends TcpClient<TestClient> {
	public TestClient(ByteBuffer buffer, AsynchronousSocketChannel channel) {
		super(buffer, channel);
	}
	
	private void handleDataChunk(DataChunk message) {
		System.out.println("received chunk");
		System.out.println(Charset.defaultCharset().decode(message.chunk()).toString());
	}
	
	@Override
	public boolean handle(TypeSwitch<Message> sw) {
		return sw
				.with(DataChunk.class, this::handleDataChunk)
				.handled() || super.handle(sw);
	}
}
