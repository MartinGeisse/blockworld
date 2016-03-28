/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.ingame;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import name.martingeisse.blockworld.client.collision.AxisAlignedCollider;
import name.martingeisse.blockworld.client.collision.CubeArrayClusterCollider;
import name.martingeisse.blockworld.client.engine.CollidingSection;
import name.martingeisse.blockworld.client.engine.RenderableSection;
import name.martingeisse.blockworld.client.engine.WorldWorkingSet;
import name.martingeisse.blockworld.client.network.ClientToServerTransmitter;
import name.martingeisse.blockworld.client.network.ServerToClientReceiver;
import name.martingeisse.blockworld.common.cubes.Cubes;
import name.martingeisse.blockworld.common.cubetype.CubeType;
import name.martingeisse.blockworld.common.geometry.ClusterSize;
import name.martingeisse.blockworld.common.geometry.SectionId;
import name.martingeisse.blockworld.common.network.c2s_message.SectionDataRequestMessage;
import name.martingeisse.blockworld.common.network.s2c_message.SectionDataResponseMessage;
import name.martingeisse.blockworld.common.network.s2c_message.SingleSectionModificationMessage;
import name.martingeisse.blockworld.common.protocol.SectionDataId;
import name.martingeisse.blockworld.common.protocol.SectionDataType;
import name.martingeisse.blockworld.common.util.task.Task;

/**
 * This class updates the set of sections in the {@link WorldWorkingSet} based
 * on the viewer's position. It requests new sections using the
 * {@link ClientToServerTransmitter} and gets response packets from
 * the {@link ServerToClientReceiver}.
 * 
 * Sending a request for sections is not initiated by this class; this class
 * only provides a method to do so. Clients should call that method in regular
 * intervals.
 * 
 * The current implementation is very simple: It keeps a cube-shaped region
 * of sections in the working set. The region contains an odd number of
 * sections along each axis, with the viewer in the middle section.
 * The section set changes whenever the viewer moves to another section.
 * In other words, the number of active sections is (2r+1)^3, with
 * r being the "radius" of the active set. The radius is a parameter for
 * the constructor of this class.
 */
public final class SectionGridLoader {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(SectionGridLoader.class);
	
	/**
	 * the workingSet
	 */
	private final WorldWorkingSet workingSet;

	/**
	 * the clientToServerTransmitter
	 */
	private final ClientToServerTransmitter clientToServerTransmitter;

	/**
	 * the renderModelRadius
	 */
	private final int renderModelRadius;

	/**
	 * the colliderRadius
	 */
	private final int colliderRadius;
	
	/**
	 * the viewerPosition
	 */
	private SectionId viewerPosition;

	/**
	 * Constructor.
	 * @param workingSet the working set to load sections for
	 * @param clientToServerTransmitter the network message transmitter
	 * @param renderModelRadius the "radius" of the active set of render models
	 * @param colliderRadius the "radius" of the active set of colliders
	 */
	public SectionGridLoader(final WorldWorkingSet workingSet, final ClientToServerTransmitter clientToServerTransmitter, final int renderModelRadius, final int colliderRadius) {
		this.workingSet = workingSet;
		this.clientToServerTransmitter = clientToServerTransmitter;
		this.renderModelRadius = renderModelRadius;
		this.colliderRadius = colliderRadius;
		this.viewerPosition = null;
	}

	/**
	 * Getter method for the viewerPosition.
	 * @return the viewerPosition
	 */
	public SectionId getViewerPosition() {
		return viewerPosition;
	}
	
	/**
	 * Setter method for the viewerPosition.
	 * @param viewerPosition the viewerPosition to set
	 */
	public void setViewerPosition(SectionId viewerPosition) {
		this.viewerPosition = viewerPosition;
	}

	/**
	 * Updates the set of visible sections, based on the viewer's position which
	 * was previously set via {@link #setViewerPosition(SectionId)} (mandatory).
	 * 
	 * @return true if anything was update-requested, false if everything stays the same
	 */
	public boolean update() {
		boolean anythingUpdated = false;
		
		// ProfilingHelper.start();
		
		// check that a position was set.
		if (viewerPosition == null) {
			throw new IllegalStateException("viewer position not set");
		}

		// remove all sections that are too far away
		if (restrictMapToRadius(workingSet.getRenderableSections(), renderModelRadius + 1)) {
			workingSet.markRenderModelsModified();
			anythingUpdated = true;
		}
		anythingUpdated |= restrictMapToRadius(workingSet.getCollidingSections(), colliderRadius + 1);
		
		// ProfilingHelper.checkRelevant("update sections 1");
		
		// detect missing section render models in the viewer's proximity, then request them all at once
		// logger.trace("checking for missing section data; position: " + viewerPosition);
		// TODO implement a batch request packet
		// TODO fetch non-interactive data for "far" sections
		{
			final List<SectionId> missingSectionIds = findMissingSectionIds(workingSet.getRenderableSections().keySet(), renderModelRadius);
			if (missingSectionIds != null && !missingSectionIds.isEmpty()) {
				final SectionId[] sectionIds = missingSectionIds.toArray(new SectionId[missingSectionIds.size()]);
				for (SectionId sectionId : sectionIds) {
					clientToServerTransmitter.transmit(new SectionDataRequestMessage(new SectionDataId(sectionId, SectionDataType.INTERACTIVE)));
					logger.debug("requested render model update for section " + sectionId);
					anythingUpdated = true;
				}
			}
		}

		// ProfilingHelper.checkRelevant("update sections 2");
		
		// detect missing section colliders in the viewer's proximity, then request them all at once
		// TODO implement a batch request packet
		{
			final List<SectionId> missingSectionIds = findMissingSectionIds(workingSet.getCollidingSections().keySet(), colliderRadius);
			if (missingSectionIds != null && !missingSectionIds.isEmpty()) {
				final SectionId[] sectionIds = missingSectionIds.toArray(new SectionId[missingSectionIds.size()]);
				for (SectionId sectionId : sectionIds) {
					clientToServerTransmitter.transmit(new SectionDataRequestMessage(new SectionDataId(sectionId, SectionDataType.INTERACTIVE)));
					logger.debug("requested collider update for section " + sectionId);
					anythingUpdated = true;
				}
			}
		}

		// ProfilingHelper.checkRelevant("update sections 3");
		
		return anythingUpdated;
	}
	
	/**
	 * Reloads a single section by requesting its render model and/or collider from the server.
	 * @param sectionId the ID of the section to reload
	 */
	public void reloadSection(SectionId sectionId) {
		clientToServerTransmitter.transmit(new SectionDataRequestMessage(new SectionDataId(sectionId, SectionDataType.INTERACTIVE)));
	}
	
	/**
	 * Handles a response message for section data that was received from the server.
	 * 
	 * @param message the message
	 */
	public void handleSectionDataResponse(SectionDataResponseMessage message) {

		// read the section data from the packet
		final SectionDataId sectionDataId = message.getSectionDataId();
		if (sectionDataId.getType() != SectionDataType.INTERACTIVE) {
			return;
		}
		final SectionId sectionId = sectionDataId.getSectionId();
		logger.debug("received interactive section image for section " + sectionId);
		final Cubes cubes = Cubes.createFromCompressedData(message.getData());
		logger.debug("created Cubes instance for section " + sectionId);
		
		// add a renderable section
		workingSet.getRenderableSectionsLoadedQueue().add(new RenderableSection(workingSet, sectionId, cubes));
		
		// add a colliding section if this section is close enough
		int dx = Math.abs(sectionId.getX() - viewerPosition.getX());
		int dy = Math.abs(sectionId.getY() - viewerPosition.getY());
		int dz = Math.abs(sectionId.getZ() - viewerPosition.getZ());
		if (dx < colliderRadius + 1 && dy < colliderRadius + 1 && dz < colliderRadius + 1) {
			new Task() {
				@Override
				public void run() {
					logger.debug("building collider for section " + sectionId);
					ClusterSize clusterSize = workingSet.getClusterSize();
					CubeType[] cubeTypes = workingSet.getEngineParameters().getCubeTypes();
					int size = clusterSize.getSize();
					byte[] colliderCubes = new byte[size * size * size];
					for (int x=0; x<size; x++) {
						for (int y=0; y<size; y++) {
							for (int z=0; z<size; z++) {
								colliderCubes[x * size * size + y * size + z] = cubes.getCubeRelative(x, y, z);
							}
						}
					}
					final AxisAlignedCollider collider = new CubeArrayClusterCollider(clusterSize, sectionId, colliderCubes, cubeTypes);
					final CollidingSection collidingSection = new CollidingSection(workingSet, sectionId, collider);
					workingSet.getCollidingSectionsLoadedQueue().add(collidingSection);
					logger.debug("collider registered for section " + sectionId);
				}
			}.schedule();
		}
		
		logger.debug("consumed interactive section image for section " + sectionId);
	}
	
	/**
	 * Handles a "single section modification event" packet that was received from the server.
	 * Note that such packets are ignored here until the player's position has been set.
	 * 
	 * TODO rename to ..message or remove
	 * 
	 * @param message the message
	 */
	public void handleModificationEventPacket(SingleSectionModificationMessage message) {
		reloadSection(message.getSectionId());
	}

	/**
	 * Removes all entries from the map whose keys are too far away from the center,
	 * currently using "city block distance" (leaving a rectangular region), not
	 * euclidian distance (which would leave a sphere).
	 * 
	 * Returns true if any entries have been removed.
	 */
	private boolean restrictMapToRadius(final Map<SectionId, ?> map, final int radius) {
		List<SectionId> idsToRemoveOld = null;
		for (final Map.Entry<SectionId, ?> entry : map.entrySet()) {
			final SectionId id = entry.getKey();
			final int dx = id.getX() - viewerPosition.getX(), dy = id.getY() - viewerPosition.getY(), dz = id.getZ() - viewerPosition.getZ();
			if (dx < -radius || dx > radius || dy < -radius || dy > radius || dz < -radius || dz > radius) {
				if (idsToRemoveOld == null) {
					idsToRemoveOld = new ArrayList<SectionId>();
				}
				idsToRemoveOld.add(id);
			}
		}
		if (idsToRemoveOld != null) {
			map.keySet().removeAll(idsToRemoveOld);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Builds a list of section IDs that are "close enough" to the center and are not yet
	 * present in the specified set of section IDs. May return null instead of an
	 * empty list.
	 */
	private List<SectionId> findMissingSectionIds(final Set<SectionId> presentSectionIds, final int radius) {
		List<SectionId> missingSectionIds = null;
		for (int dx = -radius; dx <= radius; dx++) {
			for (int dy = -radius; dy <= radius; dy++) {
				for (int dz = -radius; dz <= radius; dz++) {
					final int cx = viewerPosition.getX() + dx, cy = viewerPosition.getY() + dy, cz = viewerPosition.getZ() + dz;
					final SectionId id = new SectionId(cx, cy, cz);
					if (!presentSectionIds.contains(id)) {
						if (missingSectionIds == null) {
							missingSectionIds = new ArrayList<SectionId>();
						}
						missingSectionIds.add(id);
					}
				}
			}
		}
		return missingSectionIds;
	}

}
