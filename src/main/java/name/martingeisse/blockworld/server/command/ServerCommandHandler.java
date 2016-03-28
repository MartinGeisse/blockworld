/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.server.command;

import name.martingeisse.blockworld.server.MinerSession;

/**
 * This object handles server-side (console) commands.
 */
public interface ServerCommandHandler {

	/**
	 * Handles a command.
	 * 
	 * @param session the session of the client sending the command
	 * @param command the command
	 * @param args arguments
	 */
	public void handleCommand(MinerSession session, String command, String[] args);
	
}
