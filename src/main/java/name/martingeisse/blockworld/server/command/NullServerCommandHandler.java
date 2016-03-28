/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.server.command;

import name.martingeisse.blockworld.server.MinerSession;

/**
 * Ignores all commands.
 */
public final class NullServerCommandHandler implements ServerCommandHandler {

	// override
	@Override
	public void handleCommand(MinerSession session, String command, String[] args) {
	}

}
