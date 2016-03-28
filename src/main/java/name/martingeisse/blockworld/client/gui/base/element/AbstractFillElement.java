/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.gui.base.element;

import name.martingeisse.blockworld.client.gui.base.GuiElement;
import name.martingeisse.blockworld.client.gui.base.GuiEvent;

/**
 * Base class for elements that do not have any children and just fill
 * their available area with some graphic effect.
 */
public abstract class AbstractFillElement extends GuiElement {

	// override
	@Override
	public final void handleEvent(GuiEvent event) {
		if (event == GuiEvent.DRAW) {
			draw();
		}
	}

	// override
	@Override
	public final void requestSize(int width, int height) {
		setSize(width, height);
	}
	
	/**
	 * Draws this element.
	 */
	protected abstract void draw();
	
}
