/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.hud;

import static org.lwjgl.opengl.GL14.glWindowPos2i;
import org.lwjgl.opengl.Display;
import name.martingeisse.blockworld.client.assets.MinerResources;
import name.martingeisse.blockworld.client.glworker.GlWorkUnit;
import name.martingeisse.blockworld.client.glworker.GlWorkerLoop;
import name.martingeisse.blockworld.client.util.resource.Font;

/**
 * Draws the current frames-per-second.
 */
public class FpsHudElement implements HudElement {

	private long lastSamplingTime;
	private int countedFrames;
	private int fps;

	private final GlWorkUnit glWorkUnit = new GlWorkUnit() {
		@Override
		public void execute() {
			
			// update FPS (in the drawing thread, so we don't count skipped frames!)
			countedFrames++;
			long now = System.currentTimeMillis();
			if ((now - lastSamplingTime) >= 1000) {
				fps = countedFrames;
				countedFrames = 0;
				lastSamplingTime = now;
			}
			
			// draw the FPS panel
			String fpsText = Float.toString(fps);
			glWindowPos2i(Display.getWidth(), Display.getHeight());
			MinerResources.getInstance().getFont().drawText(fpsText, 2, Font.ALIGN_RIGHT, Font.ALIGN_TOP);
			
		}
	};
	
	/**
	 * Constructor.
	 */
	public FpsHudElement() {
		this.lastSamplingTime = System.currentTimeMillis();
		this.countedFrames = 0;
		this.fps = 0;
	}
	
	// override
	@Override
	public void draw() {
		GlWorkerLoop.getInstance().schedule(glWorkUnit);
	}
	
}
