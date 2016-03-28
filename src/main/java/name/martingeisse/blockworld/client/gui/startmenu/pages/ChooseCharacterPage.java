/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.gui.startmenu.pages;

import name.martingeisse.blockworld.client.gui.base.Gui;
import name.martingeisse.blockworld.client.gui.base.element.Spacer;
import name.martingeisse.blockworld.client.gui.base.element.VerticalLayout;
import name.martingeisse.blockworld.client.gui.startmenu.controls.StartmenuButton;
import name.martingeisse.blockworld.client.gui.startmenu.viewmodel.CharacterListEntry;
import name.martingeisse.blockworld.client.gui.startmenu.viewmodel.ViewModel;

/**
 * The "choose your character" menu page.
 */
public class ChooseCharacterPage extends AbstractStartmenuPage {

	/**
	 * Constructor.
	 * 
	 * @param viewModel the view model
	 */
	public ChooseCharacterPage(ViewModel viewModel) {
		super(viewModel);
		final VerticalLayout menu = new VerticalLayout();

		// fetch characters
		for (CharacterListEntry entry : viewModel.fetchCharacterList()) {
			menu.addElement(new StartmenuButton(entry.getName() + " (" + entry.getFaction().getDisplayName() + ")") {
				@Override
				protected void onClick() {
					getGui().setRootElement(new CharacterDetailsPage(getViewModel(), entry.getId()));
				}
			});
			menu.addElement(new Spacer(2 * Gui.GRID));
		}

		// build the remaining menu
		menu.addElement(new StartmenuButton("Create Character") {
			@Override
			protected void onClick() {
				getGui().setRootElement(new ChooseFactionPage(getViewModel()));
			}
		});
		menu.addElement(new Spacer(2 * Gui.GRID));
		menu.addElement(createExitButton());
		initializeStartmenuPage(menu);

	}

}
