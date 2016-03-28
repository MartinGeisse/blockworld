/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.common.network.s2c_message;

import com.google.common.collect.ImmutableList;
import name.martingeisse.blockworld.geometry.EulerAngles;
import name.martingeisse.blockworld.geometry.Vector3d;

/**
 * Tells the client about the list of other characters currently playing.
 */
public final class OtherCharactersUpdateMessage implements ServerToClientMessage {

	private final ImmutableList<Entry> entries;

	/**
	 * Constructor.
	 * @param entries the entries
	 */
	public OtherCharactersUpdateMessage(final ImmutableList<Entry> entries) {
		this.entries = entries;
	}

	/**
	 * Getter method for the entries.
	 * @return the entries
	 */
	public ImmutableList<Entry> getEntries() {
		return entries;
	}

	/**
	 * An entry in the character list of this message.
	 */
	public static class Entry {

		private final Vector3d position;
		private final EulerAngles orientation;
		private final String name;

		/**
		 * Constructor.
		 * @param position the player's position
		 * @param orientation the player's orientation
		 * @param name the player's name
		 */
		public Entry(final Vector3d position, final EulerAngles orientation, final String name) {
			this.position = position;
			this.orientation = orientation;
			this.name = name;
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

		/**
		 * Getter method for the name.
		 * @return the name
		 */
		public String getName() {
			return name;
		}

	}

}
