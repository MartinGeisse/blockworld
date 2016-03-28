/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.gui.base.element;

import name.martingeisse.blockworld.client.gui.base.GuiElement;
import name.martingeisse.blockworld.client.gui.base.util.AreaAlignment;
import name.martingeisse.blockworld.common.util.ParameterUtil;

/**
 * This element places its sub-elements at the same position and
 * draws one after another, overlaying the elements. If the elements
 * have different size, the alignment in this class will be used
 * to align them.
 */
public final class OverlayStack extends AbstractListElement {

	/**
	 * the alignment
	 */
	private AreaAlignment alignment;
	
	/**
	 * Constructor using CENTER alignment.
	 */
	public OverlayStack() {
		this.alignment = AreaAlignment.CENTER;
	}

	/**
	 * Getter method for the alignment.
	 * @return the alignment
	 */
	public AreaAlignment getAlignment() {
		return alignment;
	}
	
	/**
	 * Setter method for the alignment.
	 * @param alignment the alignment to set
	 * @return this for chaining
	 */
	public OverlayStack setAlignment(AreaAlignment alignment) {
		ParameterUtil.ensureNotNull(alignment, "alignment");
		this.alignment = alignment;
		requestLayout();
		return this;
	}
	
	// override
	@Override
	public void requestSize(int width, int height) {
		int requiredWidth = width, requiredHeight = height;
		for (GuiElement element : getWrappedElements()) {
			element.requestSize(width, height);
			requiredWidth = Math.max(requiredWidth, element.getWidth());
			requiredHeight = Math.max(requiredHeight, element.getHeight());
		}
		for (GuiElement element : getWrappedElements()) {
			element.requestSize(requiredWidth, requiredHeight);
		}
		setSize(requiredWidth, requiredHeight);
	}

	// override
	@Override
	protected void setChildrenLayoutPosition(int absoluteX, int absoluteY) {
		for (GuiElement element : getWrappedElements()) {
			int dx = alignment.getHorizontalAlignment().alignSpan(getWidth(), element.getWidth());
			int dy = alignment.getVerticalAlignment().alignSpan(getHeight(), element.getHeight());
			element.setPosition(absoluteX + dx, absoluteY + dy);
		}
	}
	
}
