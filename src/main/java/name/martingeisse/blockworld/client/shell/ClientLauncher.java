/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.shell;

import org.apache.log4j.Logger;
import com.google.inject.Inject;
import name.martingeisse.blockworld.client.glworker.GlWorkerLoop;
import name.martingeisse.blockworld.client.gui.startmenu.StartmenuFrameHandler;
import name.martingeisse.blockworld.client.ingame.IngameFrameHandler;
import name.martingeisse.blockworld.client.util.lwjgl.MouseUtil;

/**
 *
 */
public class ClientLauncher implements Runnable {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(ClientLauncher.class);

	private final FrameLoop frameLoop;
	@SuppressWarnings("unused")
	private final StartmenuFrameHandler startmenuFrameHandler;
	private final IngameFrameHandler ingameFrameHandler;

	/**
	 * Constructor.
	 * @param frameLoop (injected)
	 * @param startmenuFrameHandler (injected)
	 * @param ingameFrameHandler (injected)
	 */
	@Inject
	public ClientLauncher(final FrameLoop frameLoop, final StartmenuFrameHandler startmenuFrameHandler, final IngameFrameHandler ingameFrameHandler) {
		this.frameLoop = frameLoop;
		this.startmenuFrameHandler = startmenuFrameHandler;
		this.ingameFrameHandler = ingameFrameHandler;
	}

	// override
	@Override
	public void run() {
		logger.debug("Client launcher started");

		logger.trace("waiting for OpenGL worker loop to be initialized...");
		GlWorkerLoop.waitUntilInitialized();
		logger.trace("OpenGL worker loop told me it has been initialized");

		logger.trace("initializing in-game frame handler...");
		ingameFrameHandler.initialize();
		ingameFrameHandler.resumePlayer("foo"); // TODO remove when we have a start menu
		logger.trace("in-game frame handler initialized");

		// TODO remove when we have a start menu
		MouseUtil.grab();

		logger.trace("starting frame loop!");
		frameLoop.setFrameHandler(ingameFrameHandler); // TODO change to startmenuFrameHandler
		//		frameLoop.setFrameHandler(startmenuFrameHandler);
		frameLoop.executeLoop();
		GlWorkerLoop.getInstance().scheduleStop();
		logger.trace("frame loop finished!");

	}

}
