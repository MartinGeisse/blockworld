/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.standalone;

import org.apache.log4j.Logger;
import com.google.inject.Inject;
import com.google.inject.Provider;
import name.martingeisse.blockworld.server.MinerSession;
import name.martingeisse.blockworld.server.ServerLoop;
import name.martingeisse.blockworld.server.network.ClientConnectedEvent;
import name.martingeisse.blockworld.server.network.ClientDisconnectedEvent;
import name.martingeisse.blockworld.server.network.ClientToServerMessageEvent;
import name.martingeisse.blockworld.server.network.NetworkEvent;
import name.martingeisse.blockworld.server.network.NetworkEventReceiver;
import name.martingeisse.blockworld.server.network.ServerToClientTransmitter;

/**
 * Server-side fake network event receiver.
 */
public final class StandaloneServerNetworkEventReceiver implements NetworkEventReceiver {

	private static Logger logger = Logger.getLogger(StandaloneServerNetworkEventReceiver.class);

	private final ServerToClientTransmitter transmitter;
	private final StandaloneClientToServerConnection connection;
	private final Provider<ServerLoop> serverLoopProvider;
	private MinerSession session;

	/**
	 * Constructor.
	 * @param transmitter (injected)
	 * @param connection (injected)
	 * @param serverLoopProvider (injected)
	 */
	@Inject
	public StandaloneServerNetworkEventReceiver(final ServerToClientTransmitter transmitter, final StandaloneClientToServerConnection connection, final Provider<ServerLoop> serverLoopProvider) {
		this.transmitter = transmitter;
		this.connection = connection;
		this.serverLoopProvider = serverLoopProvider;
	}

	// override
	@Override
	public NetworkEvent receive() throws InterruptedException {
		while (true) {
			NetworkEvent originalEvent = connection.fetch();
			if (originalEvent instanceof ClientConnectedEvent) {
				return new ClientConnectedEvent(transmitter, s -> session = s);
			} else if (originalEvent instanceof ClientDisconnectedEvent) {
				if (session == null) {
					logger.error("singleton client disconnected before a session was assigned");
				} else {
					serverLoopProvider.get().stop();
					return new ClientDisconnectedEvent(session);
				}
			} else if (originalEvent instanceof ClientToServerMessageEvent) {
				if (session == null) {
					logger.error("singleton client sent a message before a session was assigned");
				} else {
					return new ClientToServerMessageEvent(session, ((ClientToServerMessageEvent)originalEvent).getMessage());
				}
			} else {
				logger.error("cannot bind network event to singleton session: " + originalEvent);
			}
		}
	}

}
