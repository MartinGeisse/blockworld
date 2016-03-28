/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.gui.startmenu.pages;

import java.util.prefs.Preferences;
import name.martingeisse.blockworld.client.gui.base.Gui;
import name.martingeisse.blockworld.client.gui.base.control.MessageBox;
import name.martingeisse.blockworld.client.gui.base.element.Spacer;
import name.martingeisse.blockworld.client.gui.base.element.VerticalLayout;
import name.martingeisse.blockworld.client.gui.startmenu.controls.LabeledTextField;
import name.martingeisse.blockworld.client.gui.startmenu.controls.StartmenuButton;
import name.martingeisse.blockworld.client.gui.startmenu.viewmodel.ViewModel;

/**
 * The "login" menu page.
 */
public class LoginPage extends AbstractStartmenuPage {

	/**
	 * the username
	 */
	private final LabeledTextField username;
	
	/**
	 * the password
	 */
	private final LabeledTextField password;
	
	/**
	 * Constructor.
	 * 
	 * @param viewModel the view model
	 */
	public LoginPage(ViewModel viewModel) {
		super(viewModel);
		
		Preferences preferences = Preferences.userNodeForPackage(LoginPage.class);
		String defaultUsername = preferences.get("username", null);
		if (defaultUsername == null) {
			defaultUsername = "";
		}

		username = new LabeledTextField("Username");
		password = new LabeledTextField("Password");
		username.getTextField().setNextFocusableElement(password.getTextField()).setValue(defaultUsername).moveCursorToEnd();
		password.getTextField().setNextFocusableElement(username.getTextField());
		password.getTextField().setPasswordCharacter('*');
		
		final VerticalLayout menu = new VerticalLayout();
		menu.addElement(username);
		menu.addElement(new Spacer(2 * Gui.GRID));
		menu.addElement(password);
		menu.addElement(new Spacer(2 * Gui.GRID));
		menu.addElement(new StartmenuButton("Log in") {
			@Override
			protected void onClick() {
				// TODO show a loading indicator
				getGui().addFollowupLogicAction(new Runnable() {
					@Override
					public void run() {
						login();
					}
				});
			}
		});
		menu.addElement(new Spacer(2 * Gui.GRID));
		menu.addElement(createExitButton());
		menu.addElement(new Spacer(Gui.GRID));

		initializeStartmenuPage(menu);
		
	}

	// override
	@Override
	protected void onAttach() {
		LabeledTextField initialFocus = (username.getTextField().getValue().isEmpty() ? username : password);
		getGui().setFocus(initialFocus.getTextField());
	}
	
	// override
	@Override
	protected void onEnterPressed() {
		login();
	}
	
	/**
	 * 
	 */
	private void login() {
		String username = this.username.getTextField().getValue();
		String password = this.password.getTextField().getValue();
		try {
			getViewModel().login(username, password);
			getGui().setRootElement(new ChooseCharacterPage(getViewModel()));
			Preferences.userNodeForPackage(LoginPage.class).put("username", username);
		} catch (Exception e) {
			setPopupElement(new MessageBox(e.toString()));
		}
	}

}
