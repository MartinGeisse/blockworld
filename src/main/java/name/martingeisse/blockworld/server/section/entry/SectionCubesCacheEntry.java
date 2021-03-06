/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.server.section.entry;

import name.martingeisse.blockworld.common.cubes.Cubes;
import name.martingeisse.blockworld.common.geometry.AxisAlignedDirection;
import name.martingeisse.blockworld.common.geometry.GeometryConstants;
import name.martingeisse.blockworld.common.geometry.RectangularRegion;
import name.martingeisse.blockworld.common.geometry.SectionId;
import name.martingeisse.blockworld.common.protocol.SectionDataId;
import name.martingeisse.blockworld.common.protocol.SectionDataType;
import name.martingeisse.blockworld.server.section.SectionWorkingSet;

/**
 * A section cache entry for the section data.
 */
public final class SectionCubesCacheEntry extends SectionDataCacheEntry {

	/**
	 * the region
	 */
	private final RectangularRegion region;

	/**
	 * the sectionCubes
	 */
	private Cubes sectionCubes;

	/**
	 * Constructor.
	 * @param sectionWorkingSet the working set from which this cached object comes from
	 * @param sectionDataId the section data id
	 * @param sectionCubes the section data
	 */
	public SectionCubesCacheEntry(final SectionWorkingSet sectionWorkingSet, final SectionDataId sectionDataId, final Cubes sectionCubes) {
		super(sectionWorkingSet, sectionDataId);
		SectionId sectionId = sectionDataId.getSectionId();
		this.region = new RectangularRegion(sectionId.getX(), sectionId.getY(), sectionId.getZ()).multiply(GeometryConstants.SECTION_CLUSTER_SIZE);
		this.sectionCubes = sectionCubes;
	}

	/**
	 * Getter method for the region.
	 * @return the region
	 */
	public RectangularRegion getRegion() {
		return region;
	}

	/**
	 * Getter method for the sectionCubes.
	 * @return the sectionCubes
	 */
	public Cubes getSectionCubes() {
		return sectionCubes;
	}

	// override
	@Override
	protected byte[] serializeForSave() {
		return sectionCubes.compressToByteArray();
	}

	/**
	 * Returns the cube value for the specified absolute position.
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
	 * @return the cube value
	 */
	public final byte getCubeAbsolute(final int x, final int y, final int z) {
		return getCubeRelative(x - region.getStartX(), y - region.getStartY(), z - region.getStartZ());
	}

	/**
	 * Returns the cube value for the specified section-relative position.
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
	 * @return the cube value
	 */
	public final byte getCubeRelative(final int x, final int y, final int z) {
		return sectionCubes.getCubeRelative(x, y, z);
	}

	/**
	 * Sets the cube value for the specified absolute position.
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
	 * @param value the cube value to set
	 */
	public final void setCubeAbsolute(final int x, final int y, final int z, final byte value) {
		setCubeRelative(x - region.getStartX(), y - region.getStartY(), z - region.getStartZ(), value);
	}

	/**
	 * Sets the cube value for the specified section-relative position.
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
	 * @param value the cube value to set
	 */
	public final void setCubeRelative(final int x, final int y, final int z, final byte value) {
		this.sectionCubes = sectionCubes.setCubeRelative(x, y, z, value);
		markCubeModifiedRelative(x, y, z);
	}

	/**
	 * Marks this cache entry as modified, and possibly neighbor sections as well. The cube
	 * position is specified in absolute coordinates.
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
	 */
	public final void markCubeModifiedAbsolute(final int x, final int y, final int z) {
		markCubeModifiedRelative(x - region.getStartX(), y - region.getStartY(), z - region.getStartZ());
	}

	/**
	 * Marks this cache entry as modified, and possibly neighbor sections as well. The cube
	 * position is specified relative to this section.
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
	 */
	public synchronized final void markCubeModifiedRelative(final int x, final int y, final int z) {

		// mark modified as usual
		markModified();
		
		// mark neighbor sections modified if this cube is on the border
		SectionDataId sectionDataId = getSectionDataId();
		for (AxisAlignedDirection neighborDirection : GeometryConstants.SECTION_CLUSTER_SIZE.getBorderDirections(x, y, z)) {
			// TODO there should be a way to do this without loading the to-be-invalidated object from storage
			
			SectionDataId neighborInteractiveDataId = sectionDataId.getNeighbor(neighborDirection, SectionDataType.INTERACTIVE);
			InteractiveSectionImageCacheEntry neighborInteractiveDataEntry = (InteractiveSectionImageCacheEntry)getSectionWorkingSet().get(neighborInteractiveDataId);
			neighborInteractiveDataEntry.invalidateData();
			
		}

	}

	// override
	@Override
	protected void onModification() {

		// invalidate render model and collider
		// TODO there should be a way to do this without loading the to-be-invalidated object from storage
		SectionDataId sectionDataId = getSectionDataId();
		((InteractiveSectionImageCacheEntry)getSectionWorkingSet().get(sectionDataId.getWithType(SectionDataType.INTERACTIVE))).invalidateData();

	}

	// override
	@Override
	public byte[] getDataForClient() {
		// this object cannot be sent to the client to prevent information cheating
		return new byte[0];
	}
	
}
