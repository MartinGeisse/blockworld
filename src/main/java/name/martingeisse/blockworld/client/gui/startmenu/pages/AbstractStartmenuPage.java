/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.gui.startmenu.pages;

import org.lwjgl.input.Keyboard;
import name.martingeisse.blockworld.client.assets.MinerResources;
import name.martingeisse.blockworld.client.gui.base.Gui;
import name.martingeisse.blockworld.client.gui.base.GuiElement;
import name.martingeisse.blockworld.client.gui.base.GuiEvent;
import name.martingeisse.blockworld.client.gui.base.control.MessageBox;
import name.martingeisse.blockworld.client.gui.base.control.Page;
import name.martingeisse.blockworld.client.gui.base.element.FillTexture;
import name.martingeisse.blockworld.client.gui.base.element.Margin;
import name.martingeisse.blockworld.client.gui.startmenu.controls.StartmenuButton;
import name.martingeisse.blockworld.client.gui.startmenu.viewmodel.ViewModel;
import name.martingeisse.blockworld.client.util.resource.Texture;
import name.martingeisse.blockworld.common.util.UserVisibleMessageException;

/**
 * The base class for start menu pages.
 */
public class AbstractStartmenuPage extends Page {

	private final ViewModel viewModel;

	/**
	 * Constructor.
	 * @param viewModel the view model
	 */
	public AbstractStartmenuPage(final ViewModel viewModel) {
		this.viewModel = viewModel;
	}

	/**
	 * Getter method for the viewModel.
	 * @return the viewModel
	 */
	public ViewModel getViewModel() {
		return viewModel;
	}

	/**
	 * Creates a button to exit the program.
	 *
	 * @return the exit button
	 */
	public StartmenuButton createExitButton() {
		return new StartmenuButton("Quit") {

			@Override
			protected void onClick() {
				viewModel.requestExit();
			}
		};
	}

	/**
	 *
	 */
	protected final void initializeStartmenuPage(final GuiElement mainElement) {
		final Texture backgroundTexture = MinerResources.getInstance().getCubeTextures()[2];
		initializePage(new FillTexture(backgroundTexture), new Margin(mainElement, 30 * Gui.GRID, 30 * Gui.GRID));
	}

	// override
	@Override
	protected void onException(final Throwable t) {
		if (t instanceof UserVisibleMessageException) {
			new MessageBox(t.getMessage()).show(this);
		} else {
			super.onException(t);
			new MessageBox("An error has occurred.").show(this);
		}
	}

	// override
	@Override
	protected void handlePageEvent(final GuiEvent event) {
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
