/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.server.network;

import java.util.function.Consumer;
import name.martingeisse.blockworld.server.MinerSession;

/**
 * This event occurs when a new client has connected.
 * 
 * The event contains a session linker that is used to associate a game session with
 * the connection. This linker MUST be used before receiving further events, otherwise
 * messages from that connection will be ignored because they are useless without
 * a session.
 */
public final class ClientConnectedEvent implements NetworkEvent {

	private final ServerToClientTransmitter transmitter;
	private final Consumer<MinerSession> sessionLinker;

	/**
	 * Constructor.
	 * @param transmitter the transmitter
	 * @param sessionLinker links the game session to the connection
	 */
	public ClientConnectedEvent(final ServerToClientTransmitter transmitter, final Consumer<MinerSession> sessionLinker) {
		this.transmitter = transmitter;
		this.sessionLinker = sessionLinker;
	}

	/**
	 * Getter method for the transmitter.
	 * @return the transmitter
	 */
	public ServerToClientTransmitter getTransmitter() {
		return transmitter;
	}

	/**
	 * Getter method for the sessionLinker.
	 * @return the sessionLinker
	 */
	public Consumer<MinerSession> getSessionLinker() {
		return sessionLinker;
	}

}
