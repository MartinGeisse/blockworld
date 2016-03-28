/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.server.section.entry;

import name.martingeisse.blockworld.common.geometry.RectangularRegion;
import name.martingeisse.blockworld.server.edit.IEditAccessHost;
import sun.swing.SwingUtilities2.Section;

/**
 * This is the {@link IEditAccessHost} implementation for a {@link Section}.
 * Editing uses coordinates that are absolute to the world.
 */
final class SectionAbsoluteEditAccessHost implements IEditAccessHost {

	/**
	 * the cacheEntry
	 */
	private final SectionCubesCacheEntry cacheEntry;
	
	/**
	 * Constructor.
	 * @param cacheEntry the cache entry for the section to edit
	 */
	public SectionAbsoluteEditAccessHost(final SectionCubesCacheEntry cacheEntry) {
		this.cacheEntry = cacheEntry;
	}
	
	// override
	@Override
	public boolean containsPosition(int x, int y, int z) {
		return cacheEntry.getRegion().contains(x, y, z);
	}

	// override
	@Override
	public RectangularRegion getRegion() {
		return cacheEntry.getRegion();
	}
	
	// override
	@Override
	public byte getCube(int x, int y, int z) {
		return cacheEntry.getCubeAbsolute(x, y, z);
	}
	
	// override
	@Override
	public void setCube(int x, int y, int z, byte value) {
		cacheEntry.setCubeAbsolute(x, y, z, value);
	}
	
}
