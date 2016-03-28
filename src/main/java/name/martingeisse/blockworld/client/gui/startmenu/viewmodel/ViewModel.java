/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.gui.startmenu.viewmodel;

import java.util.List;
import name.martingeisse.blockworld.common.faction.Faction;

/**
 * The view model for the start menu.
 */
public interface ViewModel {

	/**
	 * Sends username and password to the server, asking for an account access token.
	 * 
	 * @param username the username
	 * @param password the password
	 */
	public void login(final String username, final String password);
	
	/**
	 * Fetches the character list.
	 * 
	 * @return the character list
	 */
	public List<CharacterListEntry> fetchCharacterList();
	
	/**
	 * Fetches details about a character.
	 * 
	 * @param characterId the character's ID
	 * @return the character's details
	 */
	public CharacterDetails fetchCharacterDetails(String characterId);
	
	/**
	 * Getter method for the faction.
	 * @return the faction
	 */
	public Faction getFaction();

	/**
	 * Setter method for the faction.
	 * @param faction the faction to set
	 */
	public void setFaction(final Faction faction);
	
	/**
	 * Creates a character with the selected faction and the specified name.
	 * 
	 * @param name the name
	 * @return the character's ID
	 */
	public String createCharacter(String name);

	/**
	 * Deletes a character.
	 * 
	 * @param id the character's ID
	 */
	public void deleteCharacter(String id);

	/**
	 * Switches to the in-game frame handler, playing the specified character.
	 * 
	 * @param characterId the character's ID
	 */
	public void playCharacter(String characterId);
	
	/**
	 * Requests exiting the program. This does not exit immediately, but only requests exiting
	 * and actually exits at a later point, when the GUI can be cleanly shut down.
	 */
	public void requestExit();
	
	/**
	 * @return true if exiting the program was requested, false if not
	 */
	public boolean isExitRequested();
	
}
