/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.common.network.c2s_message;

import com.google.common.collect.ImmutableSet;
import name.martingeisse.blockworld.common.protocol.SectionDataId;

/**
 * Sent by the client to request section data from the server.
 */
public final class SectionDataRequestMessage implements ClientToServerMessage {

	private final ImmutableSet<SectionDataId> sectionDataIds;

	/**
	 * Constructor.
	 * @param sectionDataIds the IDs of the section datas to request
	 */
	public SectionDataRequestMessage(final ImmutableSet<SectionDataId> sectionDataIds) {
		this.sectionDataIds = sectionDataIds;
	}

	/**
	 * Getter method for the sectionDataIds.
	 * @return the sectionDataIds
	 */
	public ImmutableSet<SectionDataId> getSectionDataIds() {
		return sectionDataIds;
	}

}
