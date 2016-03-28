/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.common.network.c2s_message;

/**
 * Sent by the client to execute a server-side command.
 */
public final class ConsoleCommandMessage implements ClientToServerMessage {

	private final String command;
	private final String[] args;

	/**
	 * Constructor.
	 * @param command the command
	 * @param args arguments
	 */
	public ConsoleCommandMessage(final String command, final String[] args) {
		this.command = command;
		this.args = args;
	}

	/**
	 * Getter method for the command.
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * Getter method for the args.
	 * @return the args
	 */
	public String[] getArgs() {
		return args;
	}

}
