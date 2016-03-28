/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.ingame.player;

/**
 * Represents another player whose data gets updated from the server.
 * Updating replaces the player proxy object, so the fields in this
 * class can be final.
 *
 * TODO the "workingSet" reference in this class is currently always null.
 */
public final class PlayerProxy extends PlayerBase {

	private final String name;

	/**
	 * Constructor.
	 *
	 * @param name the player's name
	 */
	public PlayerProxy(final String name) {
		super(null);
		this.name = name;
	}

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
