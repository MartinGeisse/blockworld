/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.gui.base.element;

import name.martingeisse.blockworld.client.gui.base.GuiElement;
import name.martingeisse.blockworld.client.gui.base.GuiEvent;

/**
 * Base class for a GUI element that wraps a single other element.
 */
public abstract class AbstractWrapperElement extends GuiElement {

	/**
	 * the wrappedElement
	 */
	private GuiElement wrappedElement;

	/**
	 * Constructor.
	 */
	public AbstractWrapperElement() {
	}

	/**
	 * Constructor.
	 * @param wrappedElement the wrapped element
	 */
	public AbstractWrapperElement(GuiElement wrappedElement) {
		setWrappedElement(wrappedElement);
	}

	/**
	 * Getter method for the wrappedElement.
	 * @return the wrappedElement
	 */
	public GuiElement getWrappedElement() {
		return wrappedElement;
	}
	
	/**
	 * Setter method for the wrappedElement.
	 * @param wrappedElement the wrappedElement to set
	 * @return this for chaining
	 */
	public AbstractWrapperElement setWrappedElement(GuiElement wrappedElement) {
		this.wrappedElement = wrappedElement;
		requireWrappedElement();
		this.wrappedElement.notifyNewParent(this);
		requestLayout();
		return this;
	}

	// override
	@Override
	public void handleEvent(GuiEvent event) {
		requireWrappedElement();
		wrappedElement.handleEvent(event);
	}
	
	/**
	 * Throws an {@link IllegalStateException} if no wrapped element is currently set.
	 */
	public final void requireWrappedElement() {
		if (wrappedElement == null) {
			throw new IllegalArgumentException(getClass().getName() + " has no wrapped element set");
		}
	}

}
