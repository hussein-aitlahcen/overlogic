package com.github.overlogic;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

import com.github.overlogic.network.tcp.AbstractTcpServer;
import com.github.overlogic.util.Configuration;

public final class TestServer extends AbstractTcpServer<TestClient> {
	public TestServer(final Configuration configuration) throws Exception {
		super(configuration);
	}
	@Override
	public TestClient createClient(int identity, ByteBuffer buffer, AsynchronousSocketChannel socket) {
		return new TestClient(identity, buffer, socket);
	}
}
