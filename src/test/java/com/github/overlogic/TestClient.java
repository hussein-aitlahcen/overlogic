package com.github.overlogic;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

import com.github.overlogic.network.impl.tcp.TcpClient;

public final class TestClient extends TcpClient<TestClient> {
	public TestClient(int identity, ByteBuffer buffer, AsynchronousSocketChannel socket) {
		super(identity, buffer, socket);
	}
}
