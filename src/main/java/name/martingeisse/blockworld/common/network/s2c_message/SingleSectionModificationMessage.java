/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.common.network.s2c_message;

import name.martingeisse.blockworld.common.geometry.SectionId;

/**
 * Sent by the server when a section gets modified. Clients that
 * are close enough to be interested in the update would typically
 * request the new section render model and collider in turn. Clients
 * that are too far away would ignore these events.
 *
 * TODO: store the
 * client's rough position on the server, filter mod events
 * server-side, then just send the updated objects. This slightly
 * increases network traffic (sending unnecessary data) but reduces
 * latency and simplifies the code.
 */
public final class SingleSectionModificationMessage implements ServerToClientMessage {

	private final SectionId sectionId;

	/**
	 * Constructor.
	 * @param sectionId the section ID
	 */
	public SingleSectionModificationMessage(final SectionId sectionId) {
		this.sectionId = sectionId;
	}

	/**
	 * Getter method for the sectionId.
	 * @return the sectionId
	 */
	public SectionId getSectionId() {
		return sectionId;
	}

}
