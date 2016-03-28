/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.server.network;

import name.martingeisse.blockworld.common.network.s2c_message.ServerToClientMessage;

/**
 * A server-side per-client object that can transmit messages to the client.
 */
public interface ServerToClientTransmitter {

	/**
	 * Transmits a generic message.
	 * 
	 * @param message the message
	 */
	public void transmit(ServerToClientMessage message);
	
}
