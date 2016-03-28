/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.gui.ingame;

import name.martingeisse.blockworld.client.gui.base.Gui;
import name.martingeisse.blockworld.client.gui.base.element.Margin;
import name.martingeisse.blockworld.client.gui.base.element.Spacer;
import name.martingeisse.blockworld.client.gui.base.element.VerticalLayout;
import name.martingeisse.blockworld.client.util.lwjgl.MouseUtil;

/**
 * The "login" menu page.
 */
public abstract class MainMenuPage extends AbstractGameGuiPage {

	/**
	 * Constructor.
	 */
	public MainMenuPage() {
		final VerticalLayout menu = new VerticalLayout();
		menu.addElement(new GameGuiButton("Resume Game") {
			@Override
			protected void onClick() {
				resumeGame();
				MouseUtil.grab();
			}
		});
		menu.addElement(new Spacer(2 * Gui.GRID));
		menu.addElement(new GameGuiButton("Quit") {
			@Override
			protected void onClick() {
				quitGame();
			}
		});
		initializePage(null, new Margin(menu, 30 * Gui.GRID, 30 * Gui.GRID));
	}

	/**
	 * Called when the user clicks the "resume game" button.
	 */
	protected abstract void resumeGame();
	
	/**
	 * Called when the user clicks the "quit game" button.
	 */
	protected abstract void quitGame();
	
}
