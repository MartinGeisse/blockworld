/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.server;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import name.martingeisse.blockworld.common.geometry.GeometryConstants;
import name.martingeisse.blockworld.common.geometry.SectionId;
import name.martingeisse.blockworld.common.network.s2c_message.OtherCharactersUpdateMessage;
import name.martingeisse.blockworld.common.network.s2c_message.ServerToClientMessage;
import name.martingeisse.blockworld.common.network.s2c_message.SingleSectionModificationMessage;
import name.martingeisse.blockworld.common.protocol.SectionDataId;
import name.martingeisse.blockworld.common.protocol.SectionDataType;
import name.martingeisse.blockworld.common.util.IntervalInvoker;
import name.martingeisse.blockworld.geometry.EulerAngles;
import name.martingeisse.blockworld.geometry.Vector3d;
import name.martingeisse.blockworld.server.command.ServerCommandHandler;
import name.martingeisse.blockworld.server.game.DigUtil;
import name.martingeisse.blockworld.server.network.ClientConnectedEvent;
import name.martingeisse.blockworld.server.network.ClientDisconnectedEvent;
import name.martingeisse.blockworld.server.network.ClientToServerMessageEvent;
import name.martingeisse.blockworld.server.network.NetworkEvent;
import name.martingeisse.blockworld.server.section.SectionWorkingSet;
import name.martingeisse.blockworld.server.section.entry.SectionCubesCacheEntry;
import name.martingeisse.blockworld.server.terrain.TerrainGenerator;

/**
 * The server. Handles {@link NetworkEvent}s.
 */
public final class MinerServer {

	private static Logger logger = Logger.getLogger(MinerServer.class);

	private final SectionWorkingSet sectionWorkingSet;
	private final SectionToClientShipper sectionToClientShipper;
	private final ServerCommandHandler consoleCommandHandler;
	private final Set<MinerSession> sessions;
	private final IntervalInvoker playerListUpdateInvoker;

	/**
	 * Constructor.
	 * @param sectionWorkingSet (injected)
	 * @param sectionToClientShipper (injected)
	 * @param consoleCommandHandler (injected)
	 */
	@Inject
	public MinerServer(final SectionWorkingSet sectionWorkingSet, final SectionToClientShipper sectionToClientShipper, final ServerCommandHandler consoleCommandHandler) {
		this.sectionWorkingSet = sectionWorkingSet;
		this.sectionToClientShipper = sectionToClientShipper;
		this.consoleCommandHandler = consoleCommandHandler;
		this.sessions = new HashSet<>();
		this.playerListUpdateInvoker = new IntervalInvoker(200, () -> {
			for (final MinerSession session : sessions) {
				final List<OtherCharactersUpdateMessage.Entry> entries = new ArrayList<>();
				for (final MinerSession otherSession : sessions) {
					if (otherSession != session) {
						final Vector3d position = new Vector3d(otherSession.getX(), otherSession.getY(), otherSession.getZ());
						final EulerAngles orientation = new EulerAngles(otherSession.getLeftAngle(), otherSession.getUpAngle(), 0);
						entries.add(new OtherCharactersUpdateMessage.Entry(position, orientation, otherSession.getName()));
					}
				}
				session.sendMessage(new OtherCharactersUpdateMessage(ImmutableList.copyOf(entries)));
			}
		});
		
		// This currently happens in the main thread when Guice creates objects.
		// That's OK because letting the client connect before the world has been
		// generated is useless anyway.
		initializeWorldWithHeightField();
		
	}

	/**
	 * Handles a network event.
	 *
	 * @param event the event to handle
	 */
	public void handle(final NetworkEvent event) {
		if (event instanceof ClientConnectedEvent) {
			final ClientConnectedEvent typedEvent = (ClientConnectedEvent)event;
			final MinerSession session = new MinerSession(this, typedEvent.getTransmitter());
			sessions.add(session);
			typedEvent.getSessionLinker().accept(session);
			session.sendFlashMessage("Connected to server.");
			session.sendCoinsUpdate();
		} else if (event instanceof ClientDisconnectedEvent) {
			final ClientDisconnectedEvent typedEvent = (ClientDisconnectedEvent)event;
			typedEvent.getSession().handleDisconnect();
			sessions.remove(typedEvent.getSession());
		} else if (event instanceof ClientToServerMessageEvent) {
			final ClientToServerMessageEvent typedEvent = (ClientToServerMessageEvent)event;
			typedEvent.getSession().handleMessage(typedEvent.getMessage());
		} else {
			logger.error("unknown network event: " + event);
		}
	}

	/**
	 * Gives this server a change to perform regular tasks.
	 */
	public void step() {
		playerListUpdateInvoker.step();
	}

	/**
	 * Initializes the world using the importer to load a Minecraft map.
	 *
	 * @throws Exception on errors
	 */
	public void initializeWorldUsingImporter() throws Exception {
		logger.info("initializing world...");
		final TestRegionImporter importer = new TestRegionImporter(sectionWorkingSet.getStorage());
		// importer.setTranslation(-15, -9, -72);
		// importer.importRegions(new File("resource/fis/region"));
		importer.importRegions(new File("resource/stoneless"));
		sectionWorkingSet.clearCache();
		logger.info("world initialized");
	}

	/**
	 * Initializes the world using a Perlin noise based height field.
	 */
	public void initializeWorldWithHeightField() {
		final int horizontalRadius = 5;
		final int verticalRadius = 5;
		final TerrainGenerator terrainGenerator = new TerrainGenerator();
		terrainGenerator.generate(sectionWorkingSet.getStorage(), new SectionId(-horizontalRadius, -verticalRadius, -horizontalRadius), new SectionId(horizontalRadius, verticalRadius, horizontalRadius));
		sectionWorkingSet.clearCache();
		logger.info("world initialized");
	}

	/**
	 * @param session the session
	 * @param command the command
	 * @param args arguments
	 */
	public void handleCommand(final MinerSession session, final String command, final String[] args) {
		consoleCommandHandler.handleCommand(session, command, args);
	}

	/**
	 * @param session the session
	 * @param sectionDataId the section data ID
	 */
	public void shipSectionData(final MinerSession session, final SectionDataId sectionDataId) {
		sectionToClientShipper.addJob(sectionDataId, session);
	}

	/**
	 * Changes a single cube in the world.
	 *
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
	 * @param cubeTypeIndex the new cube type index
	 * @param session the affected session if placing / digging a cube has special effects 
	 */
	public void setCube(final int x, final int y, final int z, final byte cubeTypeIndex, MinerSession session) {
		
		// place the cube
		final SectionId sectionId = new SectionId(x >> GeometryConstants.SECTION_SHIFT, y >> GeometryConstants.SECTION_SHIFT, z >> GeometryConstants.SECTION_SHIFT);
		final SectionDataId sectionDataId = new SectionDataId(sectionId, SectionDataType.DEFINITIVE);
		final SectionCubesCacheEntry sectionDataCacheEntry = (SectionCubesCacheEntry)sectionWorkingSet.get(sectionDataId);
		byte oldCubeTypeIndex = sectionDataCacheEntry.getCubeAbsolute(x, y, z);
		if (cubeTypeIndex != 0 && oldCubeTypeIndex != 0) {
			logger.error("trying to place a cube an a cell that already has a cube");
			return;
		}
		sectionDataCacheEntry.setCubeAbsolute(x, y, z, cubeTypeIndex);
		notifyClientsAboutModifiedSections(sectionId);
		
		// if the player is digging a cube away, things may happen
		if (cubeTypeIndex == 0 && session != null) {
			DigUtil.onCubeDugAway(session, x, y, z, oldCubeTypeIndex);
		}

	}

	/**
	 * Sends a "section modified" event packet to all clients.
	 *
	 * @param sectionIds the modified section IDs
	 */
	public void notifyClientsAboutModifiedSections(final SectionId... sectionIds) {
		for (final SectionId sectionId : sectionIds) {
			broadcast(new SingleSectionModificationMessage(sectionId));
		}
	}

	/**
	 * Broadcasts a message to all clients.
	 *
	 * @param message the message
	 */
	public void broadcast(final ServerToClientMessage message) {
		for (final MinerSession session : sessions) {
			session.sendMessage(message);
		}
	}

}
