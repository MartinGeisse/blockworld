/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.standalone;

import com.google.inject.Inject;
import name.martingeisse.blockworld.client.network.ServerToClientReceiver;
import name.martingeisse.blockworld.common.network.s2c_message.ServerToClientMessage;

/**
 *
 */
public final class StandaloneServerToClientReceiver implements ServerToClientReceiver {

	private final StandaloneServerToClientConnection connection;

	/**
	 * Constructor.
	 * @param connection the connection
	 */
	@Inject
	public StandaloneServerToClientReceiver(final StandaloneServerToClientConnection connection) {
		this.connection = connection;
	}

	// override
	@Override
	public ServerToClientMessage poll() {
		return connection.poll();
	}

}
