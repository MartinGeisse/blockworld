/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.hud;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2d;
import com.google.inject.Inject;
import name.martingeisse.blockworld.client.assets.MinerResources;
import name.martingeisse.blockworld.client.glworker.GlWorkUnit;
import name.martingeisse.blockworld.client.glworker.GlWorkerLoop;
import name.martingeisse.blockworld.client.util.resource.Texture;
import name.martingeisse.blockworld.common.cubetype.CubeType;
import name.martingeisse.blockworld.common.cubetype.MinerCubeTypes;
import name.martingeisse.blockworld.common.geometry.AxisAlignedDirection;

/**
 * Shows a "selected" cube, whatever that means -- the cube
 * to display is simply set in this object.
 */
public final class SelectedCubePanel implements HudElement {

	/**
	 * the cubeTypeIndex
	 */
	private int cubeTypeIndex;

	/**
	 * the glWorkUnit
	 */
	private final GlWorkUnit glWorkUnit = new GlWorkUnit() {
		@Override
		public void execute() {
			
			Texture[] textures = MinerResources.getInstance().getCubeTextures();
			
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glEnable(GL_TEXTURE_2D);
			
			double w = 1.0, h = 1.6, d = 0.5;
			double x = -0.8, y = 0.8;
			double scale = 0.10;
			w *= scale;
			h *= scale;
			d *= scale;
			
			CubeType cubeType = MinerCubeTypes.CUBE_TYPES[cubeTypeIndex];

			textures[cubeType.getCubeFaceTextureIndex(AxisAlignedDirection.POSITIVE_Z)].glBindTexture();
			glBegin(GL_QUADS);
			glTexCoord2f(0.0f, 0.0f);
			glVertex2d(x, y-h);
			glTexCoord2f(0.0f, 1.0f);
			glVertex2d(x, y);
			glTexCoord2f(1.0f, 1.0f);
			glVertex2d(x-w, y+d);
			glTexCoord2f(1.0f, 0.0f);
			glVertex2d(x-w, y-h+d);
			glEnd();
			
			textures[cubeType.getCubeFaceTextureIndex(AxisAlignedDirection.POSITIVE_X)].glBindTexture();
			glBegin(GL_QUADS);
			glTexCoord2f(0.0f, 0.0f);
			glVertex2d(x, y);
			glTexCoord2f(0.0f, 1.0f);
			glVertex2d(x, y-h);
			glTexCoord2f(1.0f, 1.0f);
			glVertex2d(x+w, y-h+d);
			glTexCoord2f(1.0f, 0.0f);
			glVertex2d(x+w, y+d);
			glEnd();

			textures[cubeType.getCubeFaceTextureIndex(AxisAlignedDirection.POSITIVE_Y)].glBindTexture();
			glBegin(GL_QUADS);
			glTexCoord2f(0.0f, 0.0f);
			glVertex2d(x, y);
			glTexCoord2f(0.0f, 1.0f);
			glVertex2d(x+w, y+d);
			glTexCoord2f(1.0f, 1.0f);
			glVertex2d(x, y+d+d);
			glTexCoord2f(1.0f, 0.0f);
			glVertex2d(x-w, y+d);
			glEnd();
			
		}
	};
	
	/**
	 * Constructor.
	 */
	@Inject
	public SelectedCubePanel() {
	}
	
	/**
	 * Getter method for the cubeTypeIndex.
	 * @return the cubeTypeIndex
	 */
	public int getCubeTypeIndex() {
		return cubeTypeIndex;
	}
	
	/**
	 * Setter method for the cubeTypeIndex.
	 * @param cubeTypeIndex the cubeTypeIndex to set
	 */
	public void setCubeTypeIndex(int cubeTypeIndex) {
		this.cubeTypeIndex = cubeTypeIndex;
	}

	// override
	@Override
	public void draw() {
		GlWorkerLoop.getInstance().schedule(glWorkUnit);
	}

}
