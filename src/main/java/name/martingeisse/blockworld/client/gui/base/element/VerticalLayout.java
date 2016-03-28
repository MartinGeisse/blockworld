/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.gui.base.element;

import name.martingeisse.blockworld.client.gui.base.Gui;
import name.martingeisse.blockworld.client.gui.base.GuiElement;
import name.martingeisse.blockworld.client.gui.base.util.HorizontalAlignment;
import name.martingeisse.blockworld.common.util.ParameterUtil;

/**
 * This layout arranges its elements vertically, then aligns
 * them horizontally using the specified alignment.
 */
public final class VerticalLayout extends AbstractListElement {

	/**
	 * the alignment
	 */
	private HorizontalAlignment alignment;

	/**
	 * Constructor.
	 */
	public VerticalLayout() {
		this.alignment = HorizontalAlignment.CENTER;
	}

	/**
	 * Getter method for the alignment.
	 * @return the alignment
	 */
	public HorizontalAlignment getAlignment() {
		return alignment;
	}
	
	/**
	 * Setter method for the alignment.
	 * @param alignment the alignment to set
	 * @return this for chaining
	 */
	public VerticalLayout setAlignment(HorizontalAlignment alignment) {
		ParameterUtil.ensureNotNull(alignment, "alignment");
		this.alignment = alignment;
		return this;
	}

	// override
	@Override
	public void requestSize(int width, int height) {
		int requiredWidth = width, requiredHeight = 0;
		for (GuiElement element : getWrappedElements()) {
			element.requestSize(width, Gui.GRID);
			requiredWidth = Math.max(requiredWidth, element.getWidth());
			requiredHeight += element.getHeight();
		}
		setSize(requiredWidth, requiredHeight);
	}

	// override
	@Override
	protected void setChildrenLayoutPosition(int absoluteX, int absoluteY) {
		int width = getWidth();
		for (GuiElement element : getWrappedElements()) {
			element.setPosition(absoluteX + alignment.alignSpan(width, element.getWidth()), absoluteY);
			absoluteY += element.getHeight();
		}
	}

}
