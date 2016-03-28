/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.console;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL14.glWindowPos2i;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import com.google.inject.Inject;
import name.martingeisse.blockworld.client.assets.MinerResources;
import name.martingeisse.blockworld.client.glworker.GlWorkUnit;
import name.martingeisse.blockworld.client.glworker.GlWorkerLoop;
import name.martingeisse.blockworld.client.util.resource.Font;

/**
 * Renders the {@link Console} by submitting OpenGL work units
 * and forwards LWJGL keyboard events to the console.
 */
public final class ConsoleUi {

	private final Console console;

	/**
	 * the glWorkUnit
	 */
	private final GlWorkUnit glWorkUnit = new GlWorkUnit() {

		@Override
		public void execute() {
			final int screenHeight = Display.getHeight();

			// draw background
			glMatrixMode(GL11.GL_PROJECTION);
			GL11.glPushMatrix();
			glLoadIdentity();
			glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glPushMatrix();
			glLoadIdentity();
			glPolygonMode(GL_FRONT_AND_BACK, GL11.GL_FILL);
			glDisable(GL_TEXTURE_2D);
			glDisable(GL_DEPTH_TEST);
			GL11.glColor3f(0.4f, 0.4f, 0.4f);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex3i(-1, +1, 0);
			GL11.glVertex3i(+1, +1, 0);
			GL11.glVertex3i(+1, 0, 0);
			GL11.glVertex3i(-1, 0, 0);
			GL11.glEnd();
			glMatrixMode(GL11.GL_PROJECTION);
			GL11.glPopMatrix();
			glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glPopMatrix();

			// draw text
			glBindTexture(GL_TEXTURE_2D, 0);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			GL11.glPixelTransferf(GL11.GL_RED_SCALE, 0.0f);
			GL11.glPixelTransferf(GL11.GL_GREEN_SCALE, 0.0f);
			GL11.glPixelTransferf(GL11.GL_BLUE_SCALE, 0.0f);
			GL11.glPixelTransferf(GL11.GL_ALPHA_SCALE, 1.0f);
			GL11.glPixelTransferf(GL11.GL_RED_BIAS, 0.7f);
			GL11.glPixelTransferf(GL11.GL_GREEN_BIAS, 1.0f);
			GL11.glPixelTransferf(GL11.GL_BLUE_BIAS, 0.7f);
			GL11.glPixelTransferf(GL11.GL_ALPHA_BIAS, 0.0f);
			int i = 0;
			final Font font = MinerResources.getInstance().getFont();
			for (final String line : console.getCurrentOutputLines()) {
				glWindowPos2i(10, screenHeight - 10 - i * (font.getCharacterHeight() + 2));
				font.drawText(line, 1, Font.ALIGN_LEFT, Font.ALIGN_TOP);
				i++;
			}
			GL11.glPixelTransferf(GL11.GL_RED_BIAS, 1.0f);
			GL11.glPixelTransferf(GL11.GL_GREEN_BIAS, 0.7f);
			GL11.glPixelTransferf(GL11.GL_BLUE_BIAS, 0.7f);
			glWindowPos2i(10, screenHeight / 2 + font.getCharacterHeight() + 4);
			font.drawText(console.getCurrentInputLine().toString(), 1, Font.ALIGN_LEFT, Font.ALIGN_TOP);
			GL11.glPixelTransferf(GL11.GL_RED_SCALE, 1.0f);
			GL11.glPixelTransferf(GL11.GL_GREEN_SCALE, 1.0f);
			GL11.glPixelTransferf(GL11.GL_BLUE_SCALE, 1.0f);
			GL11.glPixelTransferf(GL11.GL_ALPHA_SCALE, 1.0f);
			GL11.glPixelTransferf(GL11.GL_RED_BIAS, 0.0f);
			GL11.glPixelTransferf(GL11.GL_GREEN_BIAS, 0.0f);
			GL11.glPixelTransferf(GL11.GL_BLUE_BIAS, 0.0f);
			GL11.glPixelTransferf(GL11.GL_ALPHA_BIAS, 0.0f);

		}
	};

	/**
	 * Constructor.
	 * @param console the console
	 */
	@Inject
	public ConsoleUi(final Console console) {
		this.console = console;
	}

	/**
	 * Draws the console.
	 */
	public void draw() {
		GlWorkerLoop.getInstance().schedule(glWorkUnit);
	}

	/**
	 * Consumes keyboard events from LWJGL and allows to toggle visibility.
	 *
	 * If the console is insvisible, keyboard events are still consumed but
	 * have no effect.
	 */
	public void consumeKeyboardEvents() {
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				final char c = Keyboard.getEventCharacter();
				if (c == '\n' || c == '\r') {
					console.consumeInputCharacter('\n');
				} else if (c >= 32) {
					console.consumeInputCharacter(c);
				}
			}
		}
	}

}
