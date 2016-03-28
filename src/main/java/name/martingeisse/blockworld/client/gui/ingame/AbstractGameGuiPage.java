/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.gui.ingame;

import org.lwjgl.input.Keyboard;
import name.martingeisse.blockworld.client.gui.base.GuiEvent;
import name.martingeisse.blockworld.client.gui.base.control.MessageBox;
import name.martingeisse.blockworld.client.gui.base.control.Page;
import name.martingeisse.blockworld.common.util.UserVisibleMessageException;

/**
 * The base class for start menu pages.
 */
public class AbstractGameGuiPage extends Page {

	/**
	 * Constructor.
	 */
	public AbstractGameGuiPage() {
	}
	
	// override
	@Override
	protected void onException(Throwable t) {
		if (t instanceof UserVisibleMessageException) {
			new MessageBox(t.getMessage()).show(this);
		} else {
			super.onException(t);
			new MessageBox("An error has occurred.").show(this);
		}
	}

	// override
	@Override
	protected void handlePageEvent(GuiEvent event) {
		super.handlePageEvent(event);
		if (event == GuiEvent.KEY_PRESSED && Keyboard.getEventCharacter() == '\r') {
			onEnterPressed();
		}
	}

	/**
	 * Called when the user presses the "enter" key.
	 */
	protected void onEnterPressed() {
	}
	
}
