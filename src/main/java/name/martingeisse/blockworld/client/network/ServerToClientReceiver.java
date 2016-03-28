/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.network;

import name.martingeisse.blockworld.common.network.s2c_message.ServerToClientMessage;

/**
 * A client-side object that can receive messages from the server.
 */
public interface ServerToClientReceiver {

	/**
	 * Tries to fetch the next received message.
	 * 
	 * @return the message, or null if no more messages have been received
	 */
	public ServerToClientMessage poll();

}
