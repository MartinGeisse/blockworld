/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.hud;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import java.util.Set;
import org.lwjgl.opengl.GL11;
import com.google.inject.Inject;
import name.martingeisse.blockworld.client.glworker.GlWorkUnit;
import name.martingeisse.blockworld.client.glworker.GlWorkerLoop;

/**
 * Default {@link Hud} implementation.
 */
public class DefaultHud implements Hud {

	private static final GlWorkUnit preparationUnit = () -> {
		
		// no depth testing
		glDisable(GL_DEPTH_TEST);
		
		// normal alpha blending
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		// pixel transfer coefficients set up for font rendering
		GL11.glPixelTransferf(GL11.GL_RED_SCALE, 0.0f);
		GL11.glPixelTransferf(GL11.GL_GREEN_SCALE, 0.0f);
		GL11.glPixelTransferf(GL11.GL_BLUE_SCALE, 0.0f);
		GL11.glPixelTransferf(GL11.GL_ALPHA_SCALE, 1.0f);
		GL11.glPixelTransferf(GL11.GL_RED_BIAS, 1.0f);
		GL11.glPixelTransferf(GL11.GL_GREEN_BIAS, 1.0f);
		GL11.glPixelTransferf(GL11.GL_BLUE_BIAS, 1.0f);
		GL11.glPixelTransferf(GL11.GL_ALPHA_BIAS, 0.0f);

		// texturing disabled
		glDisable(GL_TEXTURE_2D);
		
	};
	
	private final Set<HudElement> elements;

	/**
	 * Constructor.
	 * @param elements the HUD elements
	 */
	@Inject
	public DefaultHud(final Set<HudElement> elements) {
		this.elements = elements;
	}

	// override
	@Override
	public void draw() {
		for (HudElement element : elements) {
			GlWorkerLoop.getInstance().schedule(preparationUnit);
			element.draw();
		}
	}

}
