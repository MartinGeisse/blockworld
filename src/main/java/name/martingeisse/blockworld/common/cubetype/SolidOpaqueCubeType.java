/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.common.cubetype;

import java.util.Arrays;
import name.martingeisse.blockworld.common.geometry.AxisAlignedDirection;

/**
 * Cube type that is used for the typical "solid opaque" cube, like dirt, stone etc.
 * 
 * This type *cannot* be used for transparent cubes like glass!
 */
public class SolidOpaqueCubeType extends CubeType {

	/**
	 * the cubeFaceTextureIndices
	 */
	private final int[] cubeFaceTextureIndices;

	/**
	 * Constructor for cube types that use the same texture all over.
	 * @param cubeFaceTextureIndex the texture index to use for all six cube faces
	 */
	public SolidOpaqueCubeType(final int cubeFaceTextureIndex) {
		this(new int[] { cubeFaceTextureIndex, cubeFaceTextureIndex, cubeFaceTextureIndex, cubeFaceTextureIndex, cubeFaceTextureIndex, cubeFaceTextureIndex });
	}

	/**
	 * Constructor.
	 * @param cubeFaceTextureIndices the texture indices to use for the six faces, using
	 * the natural {@link AxisAlignedDirection} order.
	 */
	public SolidOpaqueCubeType(final int[] cubeFaceTextureIndices) {
		if (cubeFaceTextureIndices.length != 6) {
			throw new IllegalArgumentException("textureIndices must have length 6, has " + cubeFaceTextureIndices.length);
		}
		this.cubeFaceTextureIndices = Arrays.copyOf(cubeFaceTextureIndices, cubeFaceTextureIndices.length);
	}

	// override
	@Override
	public final boolean obscuresNeighbor(final AxisAlignedDirection direction) {
		return true;
	}

	// override
	@Override
	public boolean blocksMovementToNeighbor(final AxisAlignedDirection direction) {
		return true;
	}

	// override
	@Override
	public final int getCubeFaceTextureIndex(final int directionOrdinal) {
		return cubeFaceTextureIndices[directionOrdinal];
	}

	// override
	@Override
	public boolean collidesWithRegion(final int startX, final int startY, final int startZ, final int endX, final int endY, final int endZ) {
		return true;
	}

}
