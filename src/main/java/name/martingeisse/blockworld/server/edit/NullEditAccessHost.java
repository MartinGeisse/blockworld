/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.server.edit;

import name.martingeisse.blockworld.common.geometry.RectangularRegion;

/**
 * This implementation of {@link IEditAccessHost} does nothing and
 * returns cube type 0 for all cubes.
 */
public class NullEditAccessHost implements IEditAccessHost {

	/**
	 * the region
	 */
	private final RectangularRegion region;

	/**
	 * Constructor.
	 * @param region the region covered by this host
	 */
	public NullEditAccessHost(RectangularRegion region) {
		this.region = region;
	}
	
	// override
	@Override
	public boolean containsPosition(int x, int y, int z) {
		return region.contains(x, y, z);
	}

	// override
	@Override
	public RectangularRegion getRegion() {
		return region;
	}

	// override
	@Override
	public byte getCube(int x, int y, int z) {
		return 0;
	}

	// override
	@Override
	public void setCube(int x, int y, int z, byte value) {
	}
	
}
