package com.github.overlogic;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

import com.github.overlogic.network.impl.TcpServer;

public class TestServer extends TcpServer<TestClient> {
	public TestServer(final String host, final int port) throws IOException {
		super(host, port);
	}

	@Override
	public boolean acceptable(TestClient client) {
		return true;
	}

	@Override
	public TestClient createClient(final ByteBuffer buffer, final AsynchronousSocketChannel channel) {
		return new TestClient(buffer, channel);
	}
}
