/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.server.network;

import name.martingeisse.blockworld.common.network.c2s_message.ClientToServerMessage;
import name.martingeisse.blockworld.server.MinerSession;

/**
 * This event occurs when a client has sent a message.
 */
public final class ClientToServerMessageEvent implements NetworkEvent {

	private final MinerSession session;
	private final ClientToServerMessage message;

	/**
	 * Constructor.
	 * @param session the session
	 * @param message the message
	 */
	public ClientToServerMessageEvent(final MinerSession session, final ClientToServerMessage message) {
		this.session = session;
		this.message = message;
	}

	/**
	 * Getter method for the session.
	 * @return the session
	 */
	public MinerSession getSession() {
		return session;
	}

	/**
	 * Getter method for the message.
	 * @return the message
	 */
	public ClientToServerMessage getMessage() {
		return message;
	}

}
