/**
 * Copyright (c) 2014 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.geometry;


/**
 *
 */
public final class Vector2i extends BaseVector2i {


	/**
	 * Constructor.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public Vector2i(int x, int y) {
		super(x, y);
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof Vector2i) {
			return baseFieldsEqual((Vector2i)other);
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return baseFieldsHashCode();
	}

	/* (non-Javadoc)
	 * @@see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("{Vector2i ");
		buildBaseFieldsDescription(builder);
		builder.append('}');
		return builder.toString();
	}

}
