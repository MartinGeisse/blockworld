/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.common.cubetype;

import name.martingeisse.blockworld.common.geometry.AxisAlignedDirection;

/**
 * The special type for an empty cube. This type usually uses type index 0.
 */
public class EmptyCubeType extends CubeType {

	// override
	@Override
	public final boolean obscuresNeighbor(AxisAlignedDirection direction) {
		return false;
	}

	// override
	@Override
	public boolean blocksMovementToNeighbor(AxisAlignedDirection direction) {
		return false;
	}

	// override
	@Override
	public final int getCubeFaceTextureIndex(int directionOrdinal) {
		return 0;
	}

	// override
	@Override
	public boolean collidesWithRegion(int startX, int startY, int startZ, int endX, int endY, int endZ) {
		return false;
	}
	
}
