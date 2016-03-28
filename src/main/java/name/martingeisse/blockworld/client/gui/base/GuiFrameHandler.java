/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.gui.base;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import name.martingeisse.blockworld.client.glworker.GlWorkUnit;
import name.martingeisse.blockworld.client.glworker.GlWorkerLoop;
import name.martingeisse.blockworld.client.shell.BreakFrameLoopException;
import name.martingeisse.blockworld.client.shell.FrameHandler;

/**
 * This handler draws the GUI and sends events to it.
 */
public abstract class GuiFrameHandler implements FrameHandler {

	private Gui gui = null;

	/**
	 * the drawWorkUnit
	 */
	private final GlWorkUnit drawWorkUnit = new GlWorkUnit() {
		@Override
		public void execute() {
			drawInternal();
		}
	};
	
	/**
	 * Constructor.
	 */
	public GuiFrameHandler() {
	}
	
	// the GUI cannot be initialized in the constructor because that is called by Guice before the
	// display has been initialized
	private void lazyInitializeGui() {
		if (gui == null) {
			gui = new Gui(Display.getWidth(), Display.getHeight());
			configureGui(gui);
		}
	}
	
	/**
	 * This method is called after the GUI has been created.
	 * 
	 * @param gui the GUI
	 */
	protected abstract void configureGui(Gui gui);
	
	/**
	 * Getter method for the gui.
	 * @return the gui
	 */
	public final Gui getGui() {
		if (gui == null) {
			throw new IllegalStateException("GUI has not been initialized yet");
		}
		return gui;
	}
	
	// override
	@Override
	public void draw() {
		lazyInitializeGui();
		GlWorkerLoop.getInstance().schedule(drawWorkUnit);
	}
	
	/**
	 * Called in the OpenGL thread.
	 */
	private synchronized void drawInternal() {
		gui.fireEvent(GuiEvent.DRAW);
		gui.executeFollowupOpenglActions();
	}

	// override
	@Override
	public synchronized void step() throws BreakFrameLoopException {
		lazyInitializeGui();
		
		// dispatch keyboard events
		while (Keyboard.next()) {
			gui.fireEvent(Keyboard.getEventKeyState() ? GuiEvent.KEY_PRESSED : GuiEvent.KEY_RELEASED);
		}
		
		// dispatch mouse events
		while (Mouse.next()) {
			if (Mouse.getEventDX() != 0 || Mouse.getEventDY() != 0) {
				gui.fireEvent(GuiEvent.MOUSE_MOVED);
			}
			if (Mouse.getEventButton() != -1) {
				gui.fireEvent(Mouse.getEventButtonState() ? GuiEvent.MOUSE_BUTTON_PRESSED : GuiEvent.MOUSE_BUTTON_RELEASED);
			}
		}
		
		// handle pending followup actions
		gui.executeFollowupLogicActions();
		
	}
	
}
