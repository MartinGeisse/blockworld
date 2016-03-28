/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.server.network;

import name.martingeisse.blockworld.server.MinerSession;

/**
 * This event occurs when a new client has disconnected.
 */
public final class ClientDisconnectedEvent implements NetworkEvent {

	private final MinerSession session;

	/**
	 * Constructor.
	 * @param session the session
	 */
	public ClientDisconnectedEvent(final MinerSession session) {
		this.session = session;
	}

	/**
	 * Getter method for the session.
	 * @return the session
	 */
	public MinerSession getSession() {
		return session;
	}

}
