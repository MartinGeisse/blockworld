/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.server.section.storage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import com.google.inject.Inject;
import name.martingeisse.blockworld.common.protocol.SectionDataId;

/**
 * Pure in-memory section storage.
 */
public final class MemorySectionStorage extends AbstractSectionStorage {

	/**
	 * the storageMap
	 */
	private final ConcurrentMap<SectionDataId, byte[]> storageMap = new ConcurrentHashMap<SectionDataId, byte[]>();

	/**
	 * Constructor.
	 */
	@Inject
	public MemorySectionStorage() {
	}

	// override
	@Override
	public byte[] loadSectionRelatedObject(final SectionDataId id) {
		return storageMap.get(id);
	}

	// override
	@Override
	public Map<SectionDataId, byte[]> loadSectionRelatedObjects(final Collection<? extends SectionDataId> ids) {
		final Map<SectionDataId, byte[]> result = new HashMap<SectionDataId, byte[]>();
		for (final SectionDataId id : ids) {
			result.put(id, storageMap.get(id));
		}
		return result;
	}

	// override
	@Override
	public void saveSectionRelatedObject(final SectionDataId id, final byte[] data) {
		storageMap.put(id, data);
	}

	// override
	@Override
	public void deleteSectionRelatedObject(final SectionDataId id) {
		storageMap.remove(id);
	}

}
