/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.gui.startmenu.pages;

import name.martingeisse.blockworld.client.gui.base.Gui;
import name.martingeisse.blockworld.client.gui.base.GuiElement;
import name.martingeisse.blockworld.client.gui.base.control.MessageBox;
import name.martingeisse.blockworld.client.gui.base.element.FillColor;
import name.martingeisse.blockworld.client.gui.base.element.Margin;
import name.martingeisse.blockworld.client.gui.base.element.OverlayStack;
import name.martingeisse.blockworld.client.gui.base.element.Sizer;
import name.martingeisse.blockworld.client.gui.base.element.Spacer;
import name.martingeisse.blockworld.client.gui.base.element.TextParagraph;
import name.martingeisse.blockworld.client.gui.base.element.ThinBorder;
import name.martingeisse.blockworld.client.gui.base.element.VerticalLayout;
import name.martingeisse.blockworld.client.gui.base.util.Color;
import name.martingeisse.blockworld.client.gui.startmenu.controls.StartmenuButton;
import name.martingeisse.blockworld.client.gui.startmenu.viewmodel.CharacterDetails;
import name.martingeisse.blockworld.client.gui.startmenu.viewmodel.ViewModel;
import name.martingeisse.blockworld.client.util.lwjgl.MouseUtil;

/**
 * The "character details" menu page.
 */
public class CharacterDetailsPage extends AbstractStartmenuPage {

	private final CharacterDetails characterDetails;
	
	/**
	 * Constructor.
	 * 
	 * @param viewModel the view model
	 * @param characterId the character's ID
	 */
	public CharacterDetailsPage(ViewModel viewModel, String characterId) {
		super(viewModel);
		this.characterDetails = viewModel.fetchCharacterDetails(characterId);
		
		// build the layout
		final VerticalLayout menu = new VerticalLayout();
		menu.addElement(buildInfoBox());
		menu.addElement(new Spacer(2 * Gui.GRID));
		menu.addElement(new Sizer(new StartmenuButton("Play!") {
			@Override
			protected void onClick() {
				play();
			}
		}, -1, 10 * Gui.GRID));
		menu.addElement(new Spacer(2 * Gui.GRID));
		menu.addElement(new StartmenuButton("Delete Character") {
			@Override
			protected void onClick() {
				new MessageBox("Really delete this character?", MessageBox.YES_NO) {
					@Override
					protected void onClose(int buttonIndex) {
						if (buttonIndex == 0) {
							try {
								getViewModel().deleteCharacter(characterId);
								getGui().setRootElement(new ChooseCharacterPage(getViewModel()));
							} catch (Exception e) {
								setPopupElement(new MessageBox(e.toString()));
							}
						}
					};
				}.show(CharacterDetailsPage.this);
			}
		});
		menu.addElement(new Spacer(2 * Gui.GRID));
		menu.addElement(new Sizer(new StartmenuButton("Back") {
			@Override
			protected void onClick() {
				getGui().setRootElement(new ChooseCharacterPage(getViewModel()));
			}
		}, -1, 5 * Gui.GRID));
		initializeStartmenuPage(menu);
		
	}
	
	/**
	 * 
	 */
	private GuiElement buildInfoBox() {
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.addElement(new TextParagraph().setText("--- " + characterDetails.getName() + " ---"));
		verticalLayout.addElement(new Spacer(Gui.GRID));
		verticalLayout.addElement(new TextParagraph().setText("Faction: " + characterDetails.getFaction().getDisplayName()));
		verticalLayout.addElement(new TextParagraph().setText("Coins: " + characterDetails.getCoins()));
		OverlayStack stack = new OverlayStack();
		stack.addElement(new FillColor(new Color(128, 128, 128, 255)));
		stack.addElement(new Margin(verticalLayout, Gui.GRID));
		return new ThinBorder(stack).setColor(new Color(192, 192, 192, 255));
	}

	/**
	 * 
	 */
	private void play() {
		try {
			getViewModel().requestPlayCharacterToken(characterDetails.getId());
		} catch (Exception e) {
			setPopupElement(new MessageBox(e.toString()));
			return;
		}
		getGui().addFollowupOpenglAction(new Runnable() {
			@Override
			public void run() {
				try {
					// TODO Main.frameLoop.getRootHandler().setWrappedHandler(new IngameHandler());
					MouseUtil.grab();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

}
