/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.common.network.c2s_message;

import name.martingeisse.blockworld.common.geometry.RectangularRegion;

/**
 * Tells the server that a player had to "break free" from filled cubes.
 *
 * This message contains the affected cube locations.
 */
public final class BreakFreeMessage implements ClientToServerMessage {

	private final RectangularRegion region;

	/**
	 * Constructor.
	 * @param region the affected region
	 */
	public BreakFreeMessage(final RectangularRegion region) {
		this.region = region;
	}

	/**
	 * Getter method for the region.
	 * @return the region
	 */
	public RectangularRegion getRegion() {
		return region;
	}
	
}
