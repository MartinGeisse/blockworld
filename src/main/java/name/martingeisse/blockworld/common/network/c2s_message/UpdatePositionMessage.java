/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.common.network.c2s_message;

import name.martingeisse.blockworld.geometry.EulerAngles;
import name.martingeisse.blockworld.geometry.Vector3d;

/**
 * Updates the sender's position on the server.
 */
public final class UpdatePositionMessage implements ClientToServerMessage {

	private final Vector3d position;
	private final EulerAngles orientation;

	/**
	 * Constructor.
	 * @param position the player's position
	 * @param orientation the player's orientation
	 */
	public UpdatePositionMessage(final Vector3d position, final EulerAngles orientation) {
		this.position = position;
		this.orientation = orientation;
	}

	/**
	 * Getter method for the position.
	 * @return the position
	 */
	public Vector3d getPosition() {
		return position;
	}

	/**
	 * Getter method for the orientation.
	 * @return the orientation
	 */
	public EulerAngles getOrientation() {
		return orientation;
	}

}
