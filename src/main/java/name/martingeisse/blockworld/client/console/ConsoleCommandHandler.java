/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.console;

/**
 * The console delegates to this object to actually do something in
 * response to commands.
 */
public interface ConsoleCommandHandler {

	/**
	 * Handles a command.
	 * @param console the console passes itself here
	 * @param command the command
	 * @param args arguments
	 */
	public void handleCommand(Console console, String command, String[] args);
	
}
