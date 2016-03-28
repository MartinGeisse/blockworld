/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.gui.base.element;

import name.martingeisse.blockworld.client.gui.base.GuiElement;
import name.martingeisse.blockworld.client.gui.base.GuiEvent;

/**
 * A simple invisible element with fixed size.
 */
public final class Spacer extends GuiElement {

	/**
	 * Constructor for a square spacer.
	 * @param size the size of the spacer
	 */
	public Spacer(final int size) {
		this(size, size);
	}
	
	/**
	 * Constructor.
	 * @param width the width of the spacer
	 * @param height the height of the spacer
	 */
	public Spacer(final int width, final int height) {
		setSize(width, height);
	}

	// override
	@Override
	public void requestSize(int width, int height) {
	}

	// override
	@Override
	public void handleEvent(GuiEvent event) {
	}
	
}
