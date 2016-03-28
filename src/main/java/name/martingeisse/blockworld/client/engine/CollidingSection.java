/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.engine;

import name.martingeisse.blockworld.client.collision.AxisAlignedCollider;
import name.martingeisse.blockworld.common.geometry.ClusterSize;
import name.martingeisse.blockworld.common.geometry.RectangularRegion;
import name.martingeisse.blockworld.common.geometry.SectionId;

/**
 * This class wraps an {@link AxisAlignedCollider} for a section.
 * Instances of this class are stored for sections in the {@link WorldWorkingSet}.
 */
public final class CollidingSection implements AxisAlignedCollider {

	/**
	 * the workingSet
	 */
	private final WorldWorkingSet workingSet;

	/**
	 * the clusterSize
	 */
	private final ClusterSize clusterSize;

	/**
	 * the sectionId
	 */
	private final SectionId sectionId;

	/**
	 * the region
	 */
	private final RectangularRegion region;

	/**
	 * the RenderableSection.java
	 */
	private final AxisAlignedCollider collider;

	/**
	 * Constructor.
	 * @param workingSet the working set that contains this object
	 * @param sectionId the section id
	 * @param collider the collider
	 */
	public CollidingSection(final WorldWorkingSet workingSet, final SectionId sectionId, final AxisAlignedCollider collider) {
		this.workingSet = workingSet;
		this.clusterSize = workingSet.getClusterSize();
		this.sectionId = sectionId;
		this.region = new RectangularRegion(sectionId.getX(), sectionId.getY(), sectionId.getZ()).multiply(clusterSize);
		this.collider = collider;
	}

	/**
	 * Getter method for the workingSet.
	 * @return the workingSet
	 */
	public WorldWorkingSet getWorkingSet() {
		return workingSet;
	}

	/**
	 * Getter method for the clusterSize.
	 * @return the clusterSize
	 */
	public ClusterSize getClusterSize() {
		return clusterSize;
	}

	/**
	 * Getter method for the sectionId.
	 * @return the sectionId
	 */
	public SectionId getSectionId() {
		return sectionId;
	}

	/**
	 * Getter method for the region.
	 * @return the region
	 */
	public RectangularRegion getRegion() {
		return region;
	}

	/**
	 * Getter method for the collider.
	 * @return the collider
	 */
	public AxisAlignedCollider getCollider() {
		return collider;
	}

	// override
	@Override
	public AxisAlignedCollider getCurrentCollider() {
		return this;
	}

	// override
	@Override
	public boolean collides(final RectangularRegion region) {
		return collider.collides(region);
	}

}
