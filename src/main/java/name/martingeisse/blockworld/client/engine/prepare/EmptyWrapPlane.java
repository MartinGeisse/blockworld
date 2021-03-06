/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.engine.prepare;

import name.martingeisse.blockworld.common.cubetype.CubeType;
import name.martingeisse.blockworld.common.geometry.AxisAlignedDirection;

/**
 * {@link IWrapPlane} implementation that reports all cubes as
 * nonsolid. This is a very simple implementation that creates too many visible faces
 * in most cases. This does not produce visible artifacts but slows down rendering.
 */
public final class EmptyWrapPlane implements IWrapPlane {

	// override
	@Override
	public CubeType getCubeType(AxisAlignedDirection direction, int u, int v, CubeType[] cubeTypes) {
		return cubeTypes[0];
	}

}
