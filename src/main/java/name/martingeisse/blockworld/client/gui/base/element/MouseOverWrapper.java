/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.gui.base.element;

import name.martingeisse.blockworld.client.gui.base.GuiElement;
import name.martingeisse.blockworld.client.gui.base.GuiEvent;

/**
 * This element is only visible and only accepts elements when
 * the mouse cursor is over it. Layout is independent of the
 * mouse though.
 */
public final class MouseOverWrapper extends AbstractWrapperElement {

	/**
	 * Constructor.
	 */
	public MouseOverWrapper() {
		super();
	}

	/**
	 * Constructor.
	 * @param wrappedElement the wrapped element
	 */
	public MouseOverWrapper(final GuiElement wrappedElement) {
		super(wrappedElement);
	}

	// override
	@Override
	public void requestSize(final int width, final int height) {
		requireWrappedElement();
		getWrappedElement().requestSize(width, height);
		setSize(getWrappedElement().getWidth(), getWrappedElement().getHeight());
	}

	// override
	@Override
	protected void setChildrenLayoutPosition(int absoluteX, int absoluteY) {
		requireWrappedElement();
		getWrappedElement().setPosition(absoluteX, absoluteY);
	}

	// override
	@Override
	public void handleEvent(final GuiEvent event) {
		requireWrappedElement();
		if (isMouseInside()) {
			getWrappedElement().handleEvent(event);
		}
	}

}
