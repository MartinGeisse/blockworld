/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.gui.base.element;

import name.martingeisse.blockworld.client.gui.base.Gui;
import name.martingeisse.blockworld.client.gui.base.GuiElement;
import name.martingeisse.blockworld.client.gui.base.util.VerticalAlignment;
import name.martingeisse.blockworld.common.util.ParameterUtil;

/**
 * This layout arranges its elements horizontally, then aligns
 * them vertically using the specified alignment.
 */
public final class HorizontalLayout extends AbstractListElement {

	/**
	 * the alignment
	 */
	private VerticalAlignment alignment;

	/**
	 * Constructor.
	 */
	public HorizontalLayout() {
		this.alignment = VerticalAlignment.CENTER;
	}

	/**
	 * Getter method for the alignment.
	 * @return the alignment
	 */
	public VerticalAlignment getAlignment() {
		return alignment;
	}
	
	/**
	 * Setter method for the alignment.
	 * @param alignment the alignment to set
	 * @return this for chaining
	 */
	public HorizontalLayout setAlignment(VerticalAlignment alignment) {
		ParameterUtil.ensureNotNull(alignment, "alignment");
		this.alignment = alignment;
		return this;
	}

	// override
	@Override
	public void requestSize(int width, int height) {
		int requiredWidth = 0, requiredHeight = height;
		for (GuiElement element : getWrappedElements()) {
			element.requestSize(Gui.GRID, height);
			requiredWidth += element.getWidth();
			requiredHeight = Math.max(requiredHeight, element.getHeight());
		}
		setSize(requiredWidth, requiredHeight);
	}

	// override
	@Override
	protected void setChildrenLayoutPosition(int absoluteX, int absoluteY) {
		int height = getHeight();
		for (GuiElement element : getWrappedElements()) {
			element.setPosition(absoluteX, absoluteY + alignment.alignSpan(height, element.getHeight()));
			absoluteX += element.getWidth();
		}
	}

}
