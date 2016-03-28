/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.server.network;

/**
 * A server-side object that can receive network events, including messages
 * from clients.
 */
public interface NetworkEventReceiver {

	/**
	 * Receives the next event, waiting for an event to occur if necessary.
	 * 
	 * @return the event
	 * @throws InterruptedException when interrupted while waiting for an event
	 */
	public NetworkEvent receive() throws InterruptedException;

}
