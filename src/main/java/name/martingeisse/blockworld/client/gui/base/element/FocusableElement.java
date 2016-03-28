/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.gui.base.element;

import name.martingeisse.blockworld.client.gui.base.Gui;

/**
 * This interface is implemented by GUI elements that can have input focus.
 */
public interface FocusableElement {

	/**
	 * Notifies this element about whether it is now focused. This is
	 * used by {@link Gui#setFocus(FocusableElement)}
	 * and should not be called by other elements directly.
	 * 
	 * @param focused true if focused, false if not
	 */
	public void notifyFocus(boolean focused);
	
}
