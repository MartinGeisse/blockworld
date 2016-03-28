/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.common.network.s2c_message;

/**
 * A textual message that appears in the client.
 */
public final class FlashMessage implements ServerToClientMessage {

	private final String text;

	/**
	 * Constructor.
	 * @param text the message text
	 */
	public FlashMessage(final String text) {
		this.text = text;
	}

	/**
	 * Getter method for the text.
	 * @return the text
	 */
	public String getText() {
		return text;
	}

}
