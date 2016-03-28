/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.standalone;

import com.google.inject.Inject;
import name.martingeisse.blockworld.client.network.ClientToServerTransmitter;
import name.martingeisse.blockworld.common.network.c2s_message.ClientToServerMessage;
import name.martingeisse.blockworld.server.network.ClientConnectedEvent;
import name.martingeisse.blockworld.server.network.ClientDisconnectedEvent;
import name.martingeisse.blockworld.server.network.ClientToServerMessageEvent;

/**
 *
 */
public final class StandaloneClientToServerTransmitter implements ClientToServerTransmitter {

	private final StandaloneClientToServerConnection connection;

	/**
	 * Constructor.
	 * @param connection the connection
	 */
	@Inject
	public StandaloneClientToServerTransmitter(final StandaloneClientToServerConnection connection) {
		this.connection = connection;
		connection.add(new ClientConnectedEvent(null, null));
	}

	// override
	@Override
	public void transmit(final ClientToServerMessage message) {
		connection.add(new ClientToServerMessageEvent(null, message));
	}

	// override
	@Override
	public void disconnect() {
		connection.add(new ClientDisconnectedEvent(null));
	}

}
