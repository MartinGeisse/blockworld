/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.server.section.entry;

import name.martingeisse.blockworld.common.geometry.GeometryConstants;
import name.martingeisse.blockworld.common.geometry.RectangularRegion;
import name.martingeisse.blockworld.server.edit.IEditAccessHost;
import sun.swing.SwingUtilities2.Section;

/**
 * This is the {@link IEditAccessHost} implementation for a {@link Section}.
 * Editing uses coordinates that are relative to the section's origin.
 */
final class SectionRelativeEditAccessHost implements IEditAccessHost {

	/**
	 * the cacheEntry
	 */
	private final SectionCubesCacheEntry cacheEntry;
	
	/**
	 * Constructor.
	 * @param cacheEntry the cache entry for the section to edit
	 */
	public SectionRelativeEditAccessHost(final SectionCubesCacheEntry cacheEntry) {
		this.cacheEntry = cacheEntry;
	}
	
	// override
	@Override
	public boolean containsPosition(int x, int y, int z) {
		int size = GeometryConstants.SECTION_SIZE;
		return (x >= 0 && y >= 0 && z >= 0 && x < size && y < size && z < size);
	}

	// override
	@Override
	public RectangularRegion getRegion() {
		int size = GeometryConstants.SECTION_SIZE;
		return new RectangularRegion(0, 0, 0, size, size, size);
	}
	
	// override
	@Override
	public byte getCube(int x, int y, int z) {
		return cacheEntry.getCubeRelative(x, y, z);
	}
	
	// override
	@Override
	public void setCube(int x, int y, int z, byte value) {
		cacheEntry.setCubeRelative(x, y, z, value);
	}
	
}
