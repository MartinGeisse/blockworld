/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.standalone;

import com.google.inject.Inject;
import name.martingeisse.blockworld.common.network.s2c_message.ServerToClientMessage;
import name.martingeisse.blockworld.server.network.ServerToClientTransmitter;

/**
 *
 */
public final class StandaloneServerToClientTransmitter implements ServerToClientTransmitter {

	private final StandaloneServerToClientConnection connection;

	/**
	 * Constructor.
	 * @param connection the connection
	 */
	@Inject
	public StandaloneServerToClientTransmitter(final StandaloneServerToClientConnection connection) {
		this.connection = connection;
	}

	// override
	@Override
	public void transmit(final ServerToClientMessage message) {
		connection.send(message);
	}

}
