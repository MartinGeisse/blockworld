/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.common.network.s2c_message;

import name.martingeisse.blockworld.common.network.c2s_message.SectionDataRequestMessage;
import name.martingeisse.blockworld.common.protocol.SectionDataId;

/**
 * Sent by the server in response to a {@link SectionDataRequestMessage}. Contains
 * the requested section data.
 */
public final class SectionDataResponseMessage implements ServerToClientMessage {

	private final SectionDataId sectionDataId;
	private final byte[] data;

	/**
	 * Constructor.
	 * @param sectionDataId the ID of the section data to request
	 * @param data the returned section data
	 */
	public SectionDataResponseMessage(final SectionDataId sectionDataId, final byte[] data) {
		this.sectionDataId = sectionDataId;
		this.data = data;
	}

	/**
	 * Getter method for the sectionDataId.
	 * @return the sectionDataId
	 */
	public SectionDataId getSectionDataId() {
		return sectionDataId;
	}

	/**
	 * Getter method for the data.
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}
	
}
