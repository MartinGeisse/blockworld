/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.hud;

import name.martingeisse.blockworld.client.util.resource.Font;

/**
 * Interface used by all HUD elements.
 */
public interface HudElement {

	/**
	 * Draws this element by adding work units to the OpenGL worker loop.
	 * 
	 * This method is called with the following OpenGL state guaranteed:
	 * - depth testing disabled
	 * - normal alpha blending enabled
	 * - pixel transfer coefficients set up for font rendering (see below)
	 * - 2d texturing disabled (to avoid interfering with font rendering)
	 * 
	 * To render text, use glWindowPos() followed by
	 * {@link Font#drawText(String, float, int, int)}. The pixel transfer
	 * coefficients are prepared as scale 0 / bias 1 for the color channels
	 * and scale 1 / bias 0 for the alpha channel. This draws white text by
	 * default; change the color channel biases for different colors. The
	 * color scale is set to 0 because it doesn't work well for font
	 * rendering, due to the font not knowing the color it gets drawn with.
	 * The alpha scale 1 / bias 0 is needed to distinguish drawn pixels from
	 * transparent pixels. You can change the alpha scale to draw
	 * semi-transparent text.
	 * 
	 * The transformation state is unspecified.
	 */
	public void draw();
	
}
