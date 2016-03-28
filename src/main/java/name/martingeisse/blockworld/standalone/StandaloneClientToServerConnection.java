/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.standalone;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import name.martingeisse.blockworld.server.network.NetworkEvent;

/**
 * A network-like connection that allows the client to send {@link NetworkEvent}s
 * to the server through a synchronized queue.
 */
public final class StandaloneClientToServerConnection {

	private final BlockingQueue<NetworkEvent> eventQueue = new LinkedBlockingQueue<>();
	
	/**
	 * Adds an event to the event queue
	 * 
	 * @param event the event
	 */
	public void add(NetworkEvent event) {
		eventQueue.add(event);
	}
	
	/**
	 * If there are any events left in the event queue, then this method fetches the next
	 * one. Otherwise returns null.
	 * 
	 * @return the event or null
	 */
	public NetworkEvent poll() {
		return eventQueue.poll();
	}
	
	/**
	 * Receives an event, blocking until an event is available.
	 * 
	 * @return the event
	 * @throws InterruptedException when interrupted while waiting for an event
	 */
	public NetworkEvent fetch() throws InterruptedException {
		return eventQueue.take();
	}
	
}
