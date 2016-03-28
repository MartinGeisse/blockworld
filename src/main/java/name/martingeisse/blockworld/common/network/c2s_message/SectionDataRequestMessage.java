/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.common.network.c2s_message;

import name.martingeisse.blockworld.common.protocol.SectionDataId;

/**
 * Sent by the client to request section data from the server.
 */
public final class SectionDataRequestMessage implements ClientToServerMessage {

	private final SectionDataId sectionDataId;

	/**
	 * Constructor.
	 * @param sectionDataId the ID of the section data to request
	 */
	public SectionDataRequestMessage(final SectionDataId sectionDataId) {
		this.sectionDataId = sectionDataId;
	}

	/**
	 * Getter method for the sectionDataId.
	 * @return the sectionDataId
	 */
	public SectionDataId getSectionDataId() {
		return sectionDataId;
	}

}
