/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.gui.startmenu.viewmodel;

import java.util.Arrays;
import java.util.List;
import com.google.inject.Inject;
import name.martingeisse.blockworld.client.ingame.IngameFrameHandler;
import name.martingeisse.blockworld.client.shell.FrameLoop;
import name.martingeisse.blockworld.common.faction.Faction;

/**
 * Process-local view model implementation.
 */
public class DummyViewModel implements ViewModel {

	private final FrameLoop frameLoop;
	private final IngameFrameHandler ingameFrameHandler;

	private Faction faction;
	private boolean exitRequested;

	/**
	 * Constructor.
	 * @param frameLoop (injected)
	 * @param ingameFrameHandler (injected)
	 */
	@Inject
	public DummyViewModel(final FrameLoop frameLoop, final IngameFrameHandler ingameFrameHandler) {
		this.frameLoop = frameLoop;
		this.ingameFrameHandler = ingameFrameHandler;
	}

	// override
	@Override
	public void login(final String username, final String password) {
		if (!"username".equals(username) || !"password".equals(password)) {
			throw new RuntimeException("please use 'username' / 'password'");
		}
	}

	// override
	@Override
	public List<CharacterListEntry> fetchCharacterList() {
		return Arrays.asList(new CharacterListEntry("foo", "Gunther", Faction.THE_EMPIRE), new CharacterListEntry("bar", "Bruce Wayne", Faction.THE_BARBARIAN_CLANS));
	}

	// override
	@Override
	public CharacterDetails fetchCharacterDetails(final String characterId) {
		switch (characterId) {

			case "foo":
				return new CharacterDetails("foo", "Gunther", Faction.THE_EMPIRE, 123);

			case "bar":
				return new CharacterDetails("bar", "Bruce Wayne", Faction.THE_BARBARIAN_CLANS, 456);

			default:
				return null;

		}
	}

	// override
	@Override
	public Faction getFaction() {
		return faction;
	}

	// override
	@Override
	public void setFaction(final Faction faction) {
		this.faction = faction;
	}

	// override
	@Override
	public String createCharacter(final String name) {
		if (faction == null) {
			throw new IllegalStateException("faction not set");
		}
		throw new RuntimeException("this would create character " + name + " with faction " + faction.getDisplayName());
	}

	// override
	@Override
	public void deleteCharacter(final String characterId) {
		switch (characterId) {

			case "foo":
				throw new RuntimeException("this would delete character Gunther");

			case "bar":
				throw new RuntimeException("this would delete character Bruce Wayne");

			default:
				throw new RuntimeException("unknown character ID");

		}
	}

	// override
	@Override
	public void playCharacter(final String characterId) {
		final String playCharacterToken = characterId; // TODO use a real token
		ingameFrameHandler.resumePlayer(playCharacterToken);
		frameLoop.setFrameHandler(ingameFrameHandler);
	}

	// override
	@Override
	public void requestExit() {
		exitRequested = true;
	}

	// override
	@Override
	public boolean isExitRequested() {
		return exitRequested;
	}

}
