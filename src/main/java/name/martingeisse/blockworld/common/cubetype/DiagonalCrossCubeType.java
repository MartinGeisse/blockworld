/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.common.cubetype;

import name.martingeisse.blockworld.common.geometry.AxisAlignedDirection;

/**
 * The cube type for things that use a diagonal cross shape.
 * Examples are grass, flowers etc. This cube type is non-colliding
 * by default, but can be colliding depending on a constructor parameter.
 */
public class DiagonalCrossCubeType extends CubeType {

	/**
	 * the textureIndex
	 */
	private final int textureIndex;

	/**
	 * the solidForCollision
	 */
	private final boolean solidForCollision;

	/**
	 * Constructor.
	 * @param textureIndex the texture index
	 */
	public DiagonalCrossCubeType(final int textureIndex) {
		this.textureIndex = textureIndex;
		this.solidForCollision = false;
	}

	/**
	 * Constructor.
	 * @param textureIndex the texture index
	 * @param solidForCollision whether this cube type blocks movement
	 */
	public DiagonalCrossCubeType(final int textureIndex, final boolean solidForCollision) {
		this.textureIndex = textureIndex;
		this.solidForCollision = solidForCollision;
	}

	// override
	@Override
	public boolean obscuresNeighbor(AxisAlignedDirection direction) {
		return false;
	}
	
	// override
	@Override
	public boolean blocksMovementToNeighbor(AxisAlignedDirection direction) {
		return solidForCollision;
	}

	// override
	@Override
	public int getCubeFaceTextureIndex(int directionOrdinal) {
		return 0;
	}
	
	// override
	@Override
	public void buildInnerPolygons(MeshBuilderBase meshBuilder, int baseX, int baseY, int baseZ) {
		meshBuilder.addDiagonalCross(baseX, baseY, baseZ, baseX + 8, baseY + 8, baseZ + 8, textureIndex);
	}

	// override
	@Override
	public boolean collidesWithRegion(int startX, int startY, int startZ, int endX, int endY, int endZ) {
		return solidForCollision;
	}
	
}
