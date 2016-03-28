/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.hud;


/**
 * Represents a "HUD", i.e. all visual elements drawn over the rendered 3D image such
 * as amount of money, FPS, flash messages, etc.
 */
public interface Hud {

	/**
	 * Draws the HUD by scheduling OpenGL work units. Note that this changes various OpenGL states.
	 */
	public void draw();
	
}
