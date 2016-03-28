/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.shell;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import name.martingeisse.blockworld.client.assets.MinerResources;
import name.martingeisse.blockworld.client.glworker.GlWorkerLoop;
import name.martingeisse.blockworld.client.util.lwjgl.LwjglNativeLibraryHelper;

/**
 * Note: This launcher MUST be called by the startup thread, otherwise LWJGL will
 * screw up. Once called, the launcher won't return until the {@link GlWorkerLoop}
 * is stopped.
 */
public class OpenglLauncher implements Runnable {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(OpenglLauncher.class);

	// override
	@Override
	public void run() {
		logger.info("OpenGL Launcher started");
		final int screenWidth = 800;
		final int screenHeight = 600;
		final boolean fullscreen = false;
		try {
			
			logger.trace("setting OpenGL thread name...");
			Thread.currentThread().setName("OpenGL");
			logger.trace("OpenGL thread name set");

			logger.trace("preparing native libraries...");
			LwjglNativeLibraryHelper.prepareNativeLibraries();
			logger.trace("native libraries prepared");

			logger.trace("finding optimal display mode...");
	        DisplayMode bestMode = null;
	        int bestModeFrequency = -1;
	        for (DisplayMode mode : Display.getAvailableDisplayModes()) {
	        	if (mode.getWidth() == screenWidth && mode.getHeight() == screenHeight && (mode.isFullscreenCapable() || !fullscreen)) {
	        		if (mode.getFrequency() > bestModeFrequency) {
	        			bestMode = mode;
	        			bestModeFrequency = mode.getFrequency();
	        		}
	        	}
	        }
	        if (bestMode == null) {
	        	bestMode = new DisplayMode(screenWidth, screenHeight);
	        }
			logger.trace("optimal display mode found");
			
			logger.trace("setting intended display mode...");
	        Display.setDisplayMode(bestMode);
			if (fullscreen) {
				Display.setFullscreen(true);
			}
			logger.trace("intended display mode set");

			logger.trace("initializing display...");
			Display.create(new PixelFormat(0, 24, 0));
			logger.trace("display initialized");

			logger.trace("preparing keyboard...");
			Keyboard.create();
			Keyboard.poll();
			logger.trace("keyboard prepared");

			logger.trace("preparing mouse...");
			Mouse.create();
			Mouse.poll();
			logger.trace("mouse prepared");

			logger.trace("loading resources...");
			try {
				MinerResources.initializeInstance();
			} catch (Exception e) {
				throw new RuntimeException("could not initialize resources", e);
			}
			logger.trace("resources loaded...");
			
			logger.trace("initializing OpenGL worker loop...");
			GlWorkerLoop.initialize();
			logger.trace("OpenGL worker loop initialized");

			logger.debug("entering OpenGL worker loop now!");
			GlWorkerLoop.getInstance().workAndWait();
			logger.debug("OpenGL worker loop stopped!");
			
			logger.debug("cleaning up LWJGL...");
			Display.destroy();
			AL.destroy();
			logger.debug("LWJGL cleaned up");
			
		} catch (final Exception e) {
			logger.error("OpenGL thread crashed. Killing the process to avoid freezing the UI.", e);
			System.exit(0);
		}
	}

}
