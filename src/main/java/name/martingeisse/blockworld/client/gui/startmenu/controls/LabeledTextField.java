/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.gui.startmenu.controls;

import name.martingeisse.blockworld.client.gui.base.control.Control;
import name.martingeisse.blockworld.client.gui.base.control.TextField;
import name.martingeisse.blockworld.client.gui.base.element.TextLine;
import name.martingeisse.blockworld.client.gui.base.element.VerticalLayout;
import name.martingeisse.blockworld.client.gui.base.util.Color;
import name.martingeisse.blockworld.client.gui.base.util.HorizontalAlignment;

/**
 * A text field with a label.
 */
public final class LabeledTextField extends Control {

	/**
	 * the label
	 */
	private final TextLine label;

	/**
	 * the textField
	 */
	private final TextField textField;

	/**
	 * Constructor.
	 * @param labelText the label text
	 */
	public LabeledTextField(final String labelText) {
		this.label = new TextLine().setColor(Color.WHITE).setText(labelText);
		this.textField = new TextField();
		setControlRootElement(new VerticalLayout().setAlignment(HorizontalAlignment.LEFT).addElement(label).addElement(textField));
	}

	/**
	 * Getter method for the label.
	 * @return the label
	 */
	public TextLine getLabel() {
		return label;
	}

	/**
	 * Getter method for the textField.
	 * @return the textField
	 */
	public TextField getTextField() {
		return textField;
	}

}
