/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.common.network.s2c_message;

import com.google.common.collect.ImmutableList;

/**
 * Sent by the server to show lines of console output text in the client.
 */
public final class ConsoleOutputMessage implements ServerToClientMessage {

	private final ImmutableList<String> lines;

	/**
	 * Constructor.
	 * @param lines the output lines
	 */
	public ConsoleOutputMessage(final ImmutableList<String> lines) {
		this.lines = lines;
	}

	/**
	 * Getter method for the lines.
	 * @return the lines
	 */
	public ImmutableList<String> getLines() {
		return lines;
	}
	
}
