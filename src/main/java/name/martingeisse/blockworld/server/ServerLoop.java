/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.server;

import com.google.inject.Inject;
import name.martingeisse.blockworld.server.network.NetworkEventReceiver;

/**
 * The main loop on the server side.
 */
public final class ServerLoop {

	private final NetworkEventReceiver networkEventReceiver;
	private final MinerServer server;
	private boolean stopped;

	/**
	 * Constructor.
	 * @param networkEventReceiver (injected)
	 * @param server (injected)
	 */
	@Inject
	public ServerLoop(final NetworkEventReceiver networkEventReceiver, MinerServer server) {
		this.networkEventReceiver = networkEventReceiver;
		this.server = server;
		this.stopped = false;
	}

	/**
	 * Runs the server loop until the server shuts down.
	 * 
	 * @throws InterruptedException when interrupted while looping
	 */
	public void loop() throws InterruptedException {
		while (!stopped) {
			server.handle(networkEventReceiver.receive());
		}
	}

	/**
	 * Stops the loop.
	 */
	public void stop() {
		stopped = true;
	}
	
}
