/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.collision;

import name.martingeisse.blockworld.common.geometry.RectangularRegion;

/**
 * This collider never reports a collision.
 */
public class EmptyCollider implements AxisAlignedCollider {

	/**
	 * shared instance of this class
	 */
	public static final EmptyCollider instance = new EmptyCollider();
	
	// override
	@Override
	public AxisAlignedCollider getCurrentCollider() {
		return this;
	}
	
	// override
	@Override
	public boolean collides(RectangularRegion region) {
		return false;
	}
	
}
