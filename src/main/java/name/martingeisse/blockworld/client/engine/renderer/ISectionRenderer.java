/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.engine.renderer;

import name.martingeisse.blockworld.client.engine.FrameRenderParameters;
import name.martingeisse.blockworld.client.engine.WorldWorkingSet;
import name.martingeisse.blockworld.client.util.resource.Texture;
import name.martingeisse.blockworld.common.geometry.AxisAlignedDirection;

/**
 * Implementations of this interface are used to actually render
 * sections. This interface does not know anything about
 * sections or their render models, and instead operates on
 * low-level rendering instructions.
 * 
 * Different implementations might be used to control low-level
 * rendering features, e.g. for performance tuning.
 * 
 * {@link DefaultSectionRenderer} is the default implementation
 * that supports all features, possibly at a performance cost.
 * 
 * Implementations are usually NOT thread-safe and must be called by the thread that
 * does the high-level rendering.
 */
public interface ISectionRenderer {
	
	/**
	 * Called just before drawing the working set.
	 * @param workingSet the working set being drawn
	 * @param frameRenderParameters per-frame rendering parameters
	 */
	public void onBeforeRenderWorkingSet(WorldWorkingSet workingSet, FrameRenderParameters frameRenderParameters);
	
	/**
	 * Called just after drawing the working set.
	 * @param workingSet the working set being drawn
	 * @param frameRenderParameters per-frame rendering parameters
	 */
	public void onAfterRenderWorkingSet(WorldWorkingSet workingSet, FrameRenderParameters frameRenderParameters);

	/**
	 * Prepares for drawing with the specified texture.
	 * @param texture the texture to work with
	 */
	public void prepareForTexture(Texture texture);
	
	/**
	 * Prepares for drawing faces that are facing the specified direction.
	 * This usually configures texture coordinate generation.
	 * @param direction the direction
	 */
	public void prepareForDirection(AxisAlignedDirection direction);
	
}
