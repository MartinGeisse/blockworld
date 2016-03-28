/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.shell;

import name.martingeisse.blockworld.client.glworker.GlWorkerLoop;

/**
 * Represents the client's main callback that gets repeated every frame.
 * 
 * This class has two separated callbacks. They are called when drawing the
 * game's graphics and handling game logic, respectively. Game logic is called
 * with a fixed number of frames per second to make the game's behavior more
 * deterministic. Graphics, on the other hand, are drawn with the highest
 * possible FPS.
 */
public interface FrameHandler {

	/**
	 * Handles a game logic step.
	 * 
	 * @throws BreakFrameLoopException to break the loop (i.e. exit the game)
	 */
	public void step() throws BreakFrameLoopException;
	
	/**
	 * Draws the screen contents by passing work unis to the {@link GlWorkerLoop}.
	 */
	public void draw();

}
