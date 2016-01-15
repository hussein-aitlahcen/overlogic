package com.github.overlogic;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import com.github.overlogic.util.Configuration;

public final class TcpServerTest {
	@Test
	public final void serverConfigurationIsValid() throws Exception {		
		final String host = "127.0.0.1";
		final int port = 80;
		final int maxClients = 1000;
		final int bufferSize = 1024;		
		final TestServer server = new TestServer(new Configuration() {			
			@Override
			public void value(String key, String value) {				
			}			
			@Override
			public String value(String key) throws Exception {
				switch(key) {
					case TestServer.HOST: return host;
					case TestServer.PORT: return String.valueOf(port);
					case TestServer.MAX_CLIENTS: return String.valueOf(maxClients);
					case TestServer.BUFFER_SIZE: return String.valueOf(bufferSize);
				}
				throw new Exception(String.format("Config key % not found", key));
			}
		});		
		MatcherAssert.assertThat(
				String.format("Host should be %s", host), 
				server.host().equals(host)
		);		
		MatcherAssert.assertThat(
				String.format("Port should be %s", port), 
				server.port() == port
		);
		MatcherAssert.assertThat(
				String.format("MaxClients should be %s", maxClients), 
				server.maxClients() == maxClients
		);		
		MatcherAssert.assertThat(
				String.format("BufferSize should be %s", bufferSize), 
				server.bufferSize() == bufferSize
		);
	}
}
