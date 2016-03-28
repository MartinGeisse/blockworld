/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.gui.startmenu.viewmodel;

import name.martingeisse.blockworld.common.faction.Faction;

/**
 * Holds detailed information about a character.
 */
public final class CharacterDetails {

	private final String id;
	private final String name;
	private final Faction faction;
	private final int coins;

	/**
	 * Constructor.
	 * @param id the character's unique ID
	 * @param name the character's name
	 * @param faction the character's faction
	 * @param coins the number of coins owned by the character
	 */
	public CharacterDetails(final String id, final String name, final Faction faction, final int coins) {
		this.id = id;
		this.name = name;
		this.faction = faction;
		this.coins = coins;
	}

	/**
	 * Getter method for the id.
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter method for the faction.
	 * @return the faction
	 */
	public Faction getFaction() {
		return faction;
	}

	/**
	 * Getter method for the coins.
	 * @return the coins
	 */
	public int getCoins() {
		return coins;
	}

}
