/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.standalone;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import name.martingeisse.blockworld.common.network.s2c_message.ServerToClientMessage;

/**
 * A network-like connection that allows the server to send {@link ServerToClientMessage}s
 * to the client through a synchronized queue.
 */
public final class StandaloneServerToClientConnection {

	private final BlockingQueue<ServerToClientMessage> messageQueue = new LinkedBlockingQueue<>();
	
	/**
	 * Adds a message to the message queue
	 * 
	 * @param message the message
	 */
	public void send(ServerToClientMessage message) {
		messageQueue.add(message);
	}
	
	/**
	 * If there are any messages left in the message queue, then this method fetches the next
	 * one. Otherwise returns null.
	 * 
	 * @return the message or null
	 */
	public ServerToClientMessage poll() {
		return messageQueue.poll();
	}
	
	/**
	 * Receives a message, blocking until a message is available.
	 * 
	 * @return the message
	 * @throws InterruptedException when interrupted while waiting for a message
	 */
	public ServerToClientMessage receive() throws InterruptedException {
		return messageQueue.take();
	}
	
}
