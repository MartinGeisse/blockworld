/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.shell;

import static org.lwjgl.opengl.GL11.glFlush;
import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import name.martingeisse.blockworld.client.glworker.GlWorkUnit;
import name.martingeisse.blockworld.client.glworker.GlWorkerLoop;

/**
 * Performs a loop, calling the frame handler's {@link FrameHandler#step()}
 * method in a fixed interval and {@link FrameHandler#draw()} as often as
 * possible.
 * 
 * A {@link BreakFrameLoopException} can be thrown by the frame handler to
 * stop the loop.
 * 
 * This class passes work units for {@link Display#update()} and
 * {@link Display#processMessages()} to the OpenGL thread automatically from
 * the loop.
 * 
 * This class also checks the OpenGL thread to see if it is overloaded. If so,
 * the drawing phase is skipped to avoid throwing even more work at that thread,
 * and also to avoid work in this thread (which might share a CPU with the
 * OpenGL thread.) This class also adds frame boundary work units to the OpenGL
 * work queue to help the OpenGL thread recognize if it is overloaded.
 */
public final class FrameLoop {

	private static Logger logger = Logger.getLogger(FrameLoop.class);
	
	private static final GlWorkUnit endOfFrameWorkUnit = new GlWorkUnit() {
		@Override
		public void execute() {
			// TODO is this right? We should glFlush() to make sure everything is drawn
			// before showing that frame, but OTOH this might stall the pipeline...
			// What should actually happen is that a display-update request gets
			// passed down the pipeline and executed as soon aseverything is drawn!
			glFlush();
			Display.update();
			Display.processMessages();
		}
	};
	
	private FrameHandler frameHandler;
	private int stepInterval;
	private long lastStepTime;

	/**
	 * Constructor.
	 */
	public FrameLoop() {
		this.frameHandler = null;
		this.stepInterval = 10;
		this.lastStepTime = System.currentTimeMillis();
	}

	/**
	 * Getter method for the frameHandler.
	 * @return the frameHandler
	 */
	public synchronized FrameHandler getFrameHandler() {
		return frameHandler;
	}

	/**
	 * Setter method for the frameHandler.
	 * @param frameHandler the frameHandler to set
	 */
	public synchronized void setFrameHandler(final FrameHandler frameHandler) {
		this.frameHandler = frameHandler;
	}

	/**
	 * Getter method for the stepInterval.
	 * @return the stepInterval
	 */
	public synchronized int getStepInterval() {
		return stepInterval;
	}

	/**
	 * Setter method for the stepInterval.
	 * @param stepInterval the stepInterval to set
	 */
	public synchronized void setStepInterval(final int stepInterval) {
		if (stepInterval < 1) {
			throw new IllegalArgumentException("step interval must be positive, was: " + stepInterval);
		}
		this.stepInterval = stepInterval;
	}

	/**
	 * Enters the frame loop.
	 */
	public void executeLoop() {
		try {
			while (true) {
				
				// obtain current settings
				FrameHandler frameHandler;
				int stepInterval;
				synchronized(this) {
					frameHandler = this.frameHandler;
					stepInterval = this.stepInterval;
				}
				if (frameHandler == null) {
					throw new IllegalStateException("no frame handler set");
				}
				
				// execute game logic steps
				{
					long now = System.currentTimeMillis();
					while (now >= lastStepTime + stepInterval) {
						frameHandler.step();
						lastStepTime += stepInterval;
					}
				}
				
				// draw the game's graphics
				if (!GlWorkerLoop.getInstance().isOverloaded()) {
					frameHandler.draw();
					GlWorkerLoop.getInstance().getQueue().add(endOfFrameWorkUnit);
					GlWorkerLoop.getInstance().scheduleFrameBoundary();
				}

				// poll mouse and keyboard input in this thread to avoid changing their
				// state in the middle of the game logic
				Mouse.poll();
				Keyboard.poll();

			}
		} catch (final BreakFrameLoopException e) {
		} catch (Exception e) {
			logger.error("client thread crashed. Killing the process to avoid freezing the UI.", e);
			System.exit(0);
		}
	}
	
}
