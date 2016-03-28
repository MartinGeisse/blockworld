/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.gui.startmenu.viewmodel;

import name.martingeisse.blockworld.common.faction.Faction;

/**
 * An entry in the list of characters. Does not contain detailed information about that
 * character, only a short overview.
 */
public final class CharacterListEntry {

	private final String id;
	private final String name;
	private final Faction faction;

	/**
	 * Constructor.
	 * @param id the character's unique ID
	 * @param name the character's name
	 * @param faction the character's faction
	 */
	public CharacterListEntry(final String id, final String name, final Faction faction) {
		this.id = id;
		this.name = name;
		this.faction = faction;
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

}
