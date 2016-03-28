/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.gui.startmenu.pages;

import name.martingeisse.blockworld.client.gui.base.Gui;
import name.martingeisse.blockworld.client.gui.base.control.MessageBox;
import name.martingeisse.blockworld.client.gui.base.element.Spacer;
import name.martingeisse.blockworld.client.gui.base.element.VerticalLayout;
import name.martingeisse.blockworld.client.gui.startmenu.controls.LabeledTextField;
import name.martingeisse.blockworld.client.gui.startmenu.controls.StartmenuButton;
import name.martingeisse.blockworld.client.gui.startmenu.viewmodel.ViewModel;


/**
 * The "choose your name" menu page.
 */
public class ChooseNamePage extends AbstractStartmenuPage {
	
	/**
	 * the username
	 */
	private final LabeledTextField name;

	/**
	 * Constructor.
	 * 
	 * @param viewModel the view model
	 */
	public ChooseNamePage(ViewModel viewModel) {
		super(viewModel);
		final VerticalLayout menu = new VerticalLayout();
		this.name = new LabeledTextField("Name");
		menu.addElement(name);
		menu.addElement(new Spacer(2 * Gui.GRID));
		menu.addElement(new StartmenuButton("Create Character") {
			@Override
			protected void onClick() {
				createPlayer();
			}
		});
		menu.addElement(new Spacer(2 * Gui.GRID));
		menu.addElement(new StartmenuButton("Back") {
			@Override
			protected void onClick() {
				getGui().setRootElement(new ChooseFactionPage(getViewModel()));
			}
		});
		initializeStartmenuPage(menu);
	}

	// override
	@Override
	protected void onAttach() {
		getGui().setFocus(name.getTextField());
	}

	// override
	@Override
	protected void onEnterPressed() {
		createPlayer();
	}

	/**
	 * 
	 */
	private void createPlayer() {
		String name = ChooseNamePage.this.name.getTextField().getValue();
		try {
			String playerId = getViewModel().createCharacter(name);
			getGui().setRootElement(new CharacterDetailsPage(getViewModel(), playerId));
		} catch (Exception e) {
			setPopupElement(new MessageBox(e.toString()));
		}
	}
	
}
