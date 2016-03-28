/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.console;

/**
 * Ignores all commands.
 */
public final class NullConsoleCommandHandler implements ConsoleCommandHandler {

	// override
	@Override
	public void handleCommand(final Console console, final String command, final String[] args) {
	}

}
