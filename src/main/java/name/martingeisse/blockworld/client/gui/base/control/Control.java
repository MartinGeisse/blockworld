/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.gui.base.control;

import name.martingeisse.blockworld.client.gui.base.GuiElement;
import name.martingeisse.blockworld.client.gui.base.GuiEvent;

/**
 * Base class to build higher-level controls from primitive elements.
 */
public class Control extends GuiElement {

	/**
	 * the controlRootElement
	 */
	private GuiElement controlRootElement;

	/**
	 * Constructor.
	 */
	public Control() {
	}

	/**
	 * Getter method for the controlRootElement.
	 * @return the controlRootElement
	 */
	protected final GuiElement getControlRootElement() {
		return controlRootElement;
	}
	
	/**
	 * Setter method for the controlRootElement.
	 * @param controlRootElement the controlRootElement to set
	 */
	protected final void setControlRootElement(GuiElement controlRootElement) {
		this.controlRootElement = controlRootElement;
		if (controlRootElement != null) {
			controlRootElement.notifyNewParent(this);
		}
		requestLayout();
	}

	// override
	@Override
	public void handleEvent(GuiEvent event) {
		controlRootElement.handleEvent(event);
	}

	// override
	@Override
	public final void requestSize(int width, int height) {
		controlRootElement.requestSize(width, height);
		setSize(controlRootElement.getWidth(), controlRootElement.getHeight());
	}

	// override
	@Override
	protected final void setChildrenLayoutPosition(int absoluteX, int absoluteY) {
		controlRootElement.setPosition(absoluteX, absoluteY);
	}
	
}
