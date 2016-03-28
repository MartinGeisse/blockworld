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
import name.martingeisse.blockworld.client.gui.startmenu.viewmodel.ViewModel;
import name.martingeisse.blockworld.common.faction.Faction;

/**
 * The "choose your faction" menu page.
 */
public class ChooseFactionPage extends AbstractStartmenuPage {

	/**
	 * Constructor.
	 * 
	 * @param viewModel the view model
	 */
	public ChooseFactionPage(ViewModel viewModel) {
		super(viewModel);
		final VerticalLayout menu = new VerticalLayout();
		
		// add faction buttons
		for (final Faction faction : Faction.values()) {
			menu.addElement(new StartmenuButton(faction.getDisplayName()) {
				@Override
				protected void onClick() {
					getViewModel().setFaction(faction);
					getGui().setRootElement(new ChooseNamePage(getViewModel()));
				}
			});
			menu.addElement(new Spacer(2 * Gui.GRID));
		}
		
		// build the remaining menu
		menu.addElement(new StartmenuButton("Back") {
			@Override
			protected void onClick() {
				getGui().setRootElement(new ChooseCharacterPage(getViewModel()));
			}
		});
		initializeStartmenuPage(menu);
		
	}

}
