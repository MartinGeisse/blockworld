/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.common.network.s2c_message;

/**
 * Updates the client character's number of coins.
 */
public final class UpdateCoinsMessage implements ServerToClientMessage {

	private final long coins;

	/**
	 * Constructor.
	 * @param coins the new number of coins
	 */
	public UpdateCoinsMessage(final long coins) {
		this.coins = coins;
	}

	/**
	 * Getter method for the coins.
	 * @return the coins
	 */
	public long getCoins() {
		return coins;
	}

}
