/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.gui.base.control;

import name.martingeisse.blockworld.client.gui.base.Gui;
import name.martingeisse.blockworld.client.gui.base.GuiElement;
import name.martingeisse.blockworld.client.gui.base.GuiEvent;
import name.martingeisse.blockworld.client.gui.base.element.Margin;
import name.martingeisse.blockworld.client.gui.base.element.MouseOverWrapper;
import name.martingeisse.blockworld.client.gui.base.element.NullElement;
import name.martingeisse.blockworld.client.gui.base.element.OverlayStack;
import name.martingeisse.blockworld.client.gui.base.element.PulseFillColor;
import name.martingeisse.blockworld.client.gui.base.element.TextLine;
import name.martingeisse.blockworld.client.gui.base.element.ThinBorder;
import name.martingeisse.blockworld.client.gui.base.util.Color;
import name.martingeisse.blockworld.client.gui.base.util.PulseFunction;

/**
 * A button that can be clicked by the user.
 */
public abstract class Button extends Control {

	/**
	 * the textLine
	 */
	private final TextLine textLine;
	
	/**
	 * the margin
	 */
	private final Margin margin;
	
	/**
	 * the stack
	 */
	private final OverlayStack stack;
	
	/**
	 * the border
	 */
	private final ThinBorder border;
	
	/**
	 * Constructor.
	 */
	public Button() {
		textLine = new TextLine();
		margin = new Margin(textLine, Gui.GRID);
		stack = new OverlayStack();
		stack.addElement(NullElement.instance);
		stack.addElement(margin);
		border = new ThinBorder(stack);
		setControlRootElement(border);
	}
	
	/**
	 * Getter method for the textLine.
	 * @return the textLine
	 */
	public TextLine getTextLine() {
		return textLine;
	}
	
	/**
	 * Getter method for the margin.
	 * @return the margin
	 */
	public Margin getMargin() {
		return margin;
	}
	
	/**
	 * Getter method for the border.
	 * @return the border
	 */
	public ThinBorder getBorder() {
		return border;
	}

	/**
	 * Getter method for the backgroundElement.
	 * @return the backgroundElement
	 */
	public GuiElement getBackgroundElement() {
		return stack.getWrappedElements().get(0);
	}
	
	/**
	 * Setter method for the backgroundElement.
	 * @param backgroundElement the backgroundElement to set
	 */
	public void setBackgroundElement(GuiElement backgroundElement) {
		stack.replaceElement(0, backgroundElement);
	}

	/**
	 * Adds a pulse effect that is visible when the mouse is over the button.
	 * This method doesn't take a pulse amplitude; use the color's alpha
	 * channel for that.
	 * 
	 * @param color the pulse color
	 * @return this button for chaining
	 */
	public Button addPulseEffect(Color color) {
		return addPulseEffect(color, PulseFunction.ABSOLUTE_SINE, 2000);
	}
	
	/**
	 * Adds a pulse effect that is visible when the mouse is over the button.
	 * This method doesn't take a pulse amplitude; use the color's alpha
	 * channel for that.
	 * 
	 * @param color the pulse color
	 * @param function the pulse function
	 * @param period the pulse period
	 * @return this button for chaining
	 */
	public Button addPulseEffect(Color color, PulseFunction function, int period) {
		PulseFillColor fillColor = new PulseFillColor().setColor(color).setPulseFunction(function).setPeriod(period);
		stack.addElement(new MouseOverWrapper(fillColor));
		return this;
	}

	// override
	@Override
	public void handleEvent(GuiEvent event) {
		super.handleEvent(event);
		if (event == GuiEvent.MOUSE_BUTTON_PRESSED && isMouseInside()) {
			onClick();
		}
	}
	
	/**
	 * This method gets called when the user clicks on the button.
	 */
	protected abstract void onClick();
	
}
