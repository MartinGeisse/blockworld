/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.gui.startmenu;

import com.google.inject.Inject;
import name.martingeisse.blockworld.client.assets.MinerResources;
import name.martingeisse.blockworld.client.gui.base.Gui;
import name.martingeisse.blockworld.client.gui.base.GuiFrameHandler;
import name.martingeisse.blockworld.client.gui.startmenu.pages.LoginPage;
import name.martingeisse.blockworld.client.gui.startmenu.viewmodel.ViewModel;
import name.martingeisse.blockworld.client.shell.BreakFrameLoopException;

/**
 * TODO refactor the start menu to use onException()
 * of the page object
 */
public class StartmenuFrameHandler extends GuiFrameHandler {

	private final ViewModel viewModel;

	/**
	 * Constructor.
	 * @param viewModel (injected)
	 */
	@Inject
	public StartmenuFrameHandler(final ViewModel viewModel) {
		this.viewModel = viewModel;
	}

	// override
	@Override
	protected void configureGui(final Gui gui) {
		gui.setDefaultFont(MinerResources.getInstance().getFont());
		gui.setRootElement(new LoginPage(viewModel));
	}

	// override
	@Override
	public synchronized void step() throws BreakFrameLoopException {
		super.step();
		if (viewModel.isExitRequested()) {
			throw new BreakFrameLoopException();
		}
	}
	
}
