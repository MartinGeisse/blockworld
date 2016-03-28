/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.common.network.c2s_message;

/**
 * Tells the server about a single-cube modification.
 */
public final class SingleCubeModificationMessage implements ClientToServerMessage {

	private final int x;
	private final int y;
	private final int z;
	private final byte newCubeTypeIndex;

	/**
	 * Constructor.
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
	 * @param newCubeTypeIndex the new cube type index
	 */
	public SingleCubeModificationMessage(final int x, final int y, final int z, final byte newCubeTypeIndex) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.newCubeTypeIndex = newCubeTypeIndex;
	}

	/**
	 * Getter method for the x.
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Getter method for the y.
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Getter method for the z.
	 * @return the z
	 */
	public int getZ() {
		return z;
	}

	/**
	 * Getter method for the newCubeTypeIndex.
	 * @return the newCubeTypeIndex
	 */
	public byte getNewCubeTypeIndex() {
		return newCubeTypeIndex;
	}

}
