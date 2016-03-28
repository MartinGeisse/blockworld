/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.network;

import name.martingeisse.blockworld.common.network.c2s_message.ClientToServerMessage;

/**
 * A client-side object that can transmit messages to the server.
 */
public interface ClientToServerTransmitter {

	/**
	 * Transmits a generic message.
	 * 
	 * @param message the message
	 */
	public void transmit(ClientToServerMessage message);

	/**
	 * Disconnects from the server.
	 */
	public void disconnect();
	
}
