/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.common.network.s2c_message;

import com.google.common.collect.ImmutableMap;
import name.martingeisse.blockworld.common.network.c2s_message.SectionDataRequestMessage;
import name.martingeisse.blockworld.common.protocol.SectionDataId;

/**
 * Sent by the server in response to a {@link SectionDataRequestMessage}. Contains
 * the requested section data.
 */
public final class SectionDataResponseMessage implements ServerToClientMessage {

	private final ImmutableMap<SectionDataId, byte[]> dataBySectionDataId;

	/**
	 * Constructor.
	 * @param dataBySectionDataId a map of sction data ID to requested data
	 */
	public SectionDataResponseMessage(final ImmutableMap<SectionDataId, byte[]> dataBySectionDataId) {
		this.dataBySectionDataId = dataBySectionDataId;
	}

	/**
	 * Getter method for the dataBySectionDataId.
	 * @return the dataBySectionDataId
	 */
	public ImmutableMap<SectionDataId, byte[]> getDataBySectionDataId() {
		return dataBySectionDataId;
	}

}
