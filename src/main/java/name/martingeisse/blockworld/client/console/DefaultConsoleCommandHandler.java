/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.console;

import com.google.inject.Inject;
import name.martingeisse.blockworld.client.glworker.GlWorkerLoop;
import name.martingeisse.blockworld.client.network.ClientToServerTransmitter;
import name.martingeisse.blockworld.client.shell.BreakFrameLoopException;
import name.martingeisse.blockworld.common.network.c2s_message.ConsoleCommandMessage;

/**
 *
 */
public class DefaultConsoleCommandHandler implements ConsoleCommandHandler {

	private final ClientToServerTransmitter transmitter;

	/**
	 * Constructor.
	 * @param transmitter (injected)
	 */
	@Inject
	public DefaultConsoleCommandHandler(final ClientToServerTransmitter transmitter) {
		this.transmitter = transmitter;
	}

	// override
	@Override
	public void handleCommand(final Console console, final String command, final String[] args) {
		if (command.equals("quit")) {
			throw new BreakFrameLoopException();
		} else if (command.equals("initworld") || command.equals("help") || command.equals("itemtypes") || command.equals("wish") || command.equals("equip") || command.equals("unequip") || command.equals("trash") || command.equals("give") || command.equals("inventory")) {
			transmitter.transmit(new ConsoleCommandMessage(command, args));
		} else if (command.equals("echo")) {
			for (final String arg : args) {
				console.println(arg);
			}
		} else {
			console.println("unknown command: " + command);
		}
	}

}
