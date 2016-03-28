/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.server;

import java.util.Collection;
import org.apache.log4j.Logger;
import com.google.common.collect.ImmutableList;
import name.martingeisse.blockworld.common.network.c2s_message.BreakFreeMessage;
import name.martingeisse.blockworld.common.network.c2s_message.ClientToServerMessage;
import name.martingeisse.blockworld.common.network.c2s_message.ConsoleCommandMessage;
import name.martingeisse.blockworld.common.network.c2s_message.ResumeCharacterMessage;
import name.martingeisse.blockworld.common.network.c2s_message.SectionDataRequestMessage;
import name.martingeisse.blockworld.common.network.c2s_message.SingleCubeModificationMessage;
import name.martingeisse.blockworld.common.network.c2s_message.UpdatePositionMessage;
import name.martingeisse.blockworld.common.network.s2c_message.CharacterResumedMessage;
import name.martingeisse.blockworld.common.network.s2c_message.ConsoleOutputMessage;
import name.martingeisse.blockworld.common.network.s2c_message.FlashMessage;
import name.martingeisse.blockworld.common.network.s2c_message.ServerToClientMessage;
import name.martingeisse.blockworld.common.network.s2c_message.UpdateCoinsMessage;
import name.martingeisse.blockworld.common.protocol.SectionDataId;
import name.martingeisse.blockworld.common.protocol.SectionDataType;
import name.martingeisse.blockworld.geometry.EulerAngles;
import name.martingeisse.blockworld.geometry.Vector3d;
import name.martingeisse.blockworld.server.network.ServerToClientTransmitter;

/**
 * Stores the data for one user session (currently associated with the connection,
 * but intended to service connection dropping and re-connecting).
 */
public class MinerSession {

	private static Logger logger = Logger.getLogger(MinerSession.class);

	private final MinerServer server;
	private final ServerToClientTransmitter transmitter;
	private volatile String characterId;
	private volatile double x;
	private volatile double y;
	private volatile double z;
	private volatile double leftAngle;
	private volatile double upAngle;
	private volatile String name;
	private volatile long coins;

	/**
	 * Constructor.
	 * @param server the server
	 * @param transmitter the transmitter that sends messages to the client
	 */
	public MinerSession(MinerServer server, ServerToClientTransmitter transmitter) {
		this.server = server;
		this.transmitter = transmitter;
		this.name = "Player";
	}

	/**
	 * Getter method for the transmitter.
	 * @return the transmitter
	 */
	public ServerToClientTransmitter getTransmitter() {
		return transmitter;
	}

	/**
	 * Getter method for the characterId.
	 * @return the characterId
	 */
	public String getCharacterId() {
		return characterId;
	}

	/**
	 * Setter method for the characterId.
	 * @param characterId the characterId to set
	 */
	public void setCharacterId(final String characterId) {
		this.characterId = characterId;
	}

	/**
	 * Getter method for the x.
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * Setter method for the x.
	 * @param x the x to set
	 */
	public void setX(final double x) {
		this.x = x;
	}

	/**
	 * Getter method for the y.
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * Setter method for the y.
	 * @param y the y to set
	 */
	public void setY(final double y) {
		this.y = y;
	}

	/**
	 * Getter method for the z.
	 * @return the z
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Setter method for the z.
	 * @param z the z to set
	 */
	public void setZ(final double z) {
		this.z = z;
	}

	/**
	 * Getter method for the leftAngle.
	 * @return the leftAngle
	 */
	public double getLeftAngle() {
		return leftAngle;
	}

	/**
	 * Setter method for the leftAngle.
	 * @param leftAngle the leftAngle to set
	 */
	public void setLeftAngle(final double leftAngle) {
		this.leftAngle = leftAngle;
	}

	/**
	 * Getter method for the upAngle.
	 * @return the upAngle
	 */
	public double getUpAngle() {
		return upAngle;
	}

	/**
	 * Setter method for the upAngle.
	 * @param upAngle the upAngle to set
	 */
	public void setUpAngle(final double upAngle) {
		this.upAngle = upAngle;
	}

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method for the name.
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Getter method for the coins.
	 * @return the coins
	 */
	public long getCoins() {
		return coins;
	}

	/**
	 * Setter method for the coins.
	 * @param coins the coins to set
	 */
	public void setCoins(final long coins) {
		this.coins = coins;
	}

	/**
	 * Sends a network message to the client that owns this session.
	 *
	 * @param message the message to send
	 */
	public final void sendMessage(final ServerToClientMessage message) {
		transmitter.transmit(message);
	}

	/**
	 * Sends a flash message to the client that owns this session.
	 * @param message the message
	 */
	public final void sendFlashMessage(final String message) {
		sendMessage(new FlashMessage(message));
	}

	/**
	 * Sends console output lines to the client.
	 * @param lines the lines to send
	 */
	public final void sendConsoleOutput(final Collection<String> lines) {
		sendMessage(new ConsoleOutputMessage(ImmutableList.copyOf(lines)));
	}

	/**
	 * Sends console output lines to the client.
	 * @param lines the lines to send
	 */
	public final void sendConsoleOutput(final String... lines) {
		sendMessage(new ConsoleOutputMessage(ImmutableList.copyOf(lines)));
	}

	/**
	 * Handles disconnected clients.
	 */
	public void handleDisconnect() {
		// TODO
		//		if (playerId != null) {
		//			final SQLUpdateClause update = EntityConnectionManager.getConnection().createUpdate(QPlayer.player);
		//			update.where(QPlayer.player.id.eq(playerId));
		//			update.set(QPlayer.player.x, new BigDecimal(x));
		//			update.set(QPlayer.player.y, new BigDecimal(y));
		//			update.set(QPlayer.player.z, new BigDecimal(z));
		//			update.set(QPlayer.player.leftAngle, new BigDecimal(leftAngle));
		//			update.set(QPlayer.player.upAngle, new BigDecimal(upAngle));
		//			try {
		//				update.execute();
		//			} catch (final Exception e) {
		//				logger.error("could not update player position. id: " + playerId + "; values = " + x + ", " + y + ", " + z + ", " + leftAngle + ", " + upAngle);
		//			}
		//		}
	}

	/**
	 * Sends an update for the number of coins to the client.
	 */
	public void sendCoinsUpdate() {
		sendMessage(new UpdateCoinsMessage(coins));
	}

	/**
	 * Handles a message from the client.
	 *
	 * @param message the message to handle
	 */
	public void handleMessage(final ClientToServerMessage message) {
		if (message instanceof ResumeCharacterMessage) {
			final String playCharacterToken = ((ResumeCharacterMessage)message).getPlayCharacterToken();
			characterId = playCharacterToken; // TODO use a real token
			// TODO load properties from player database
			name = "player";
			x = 0;
			y = 0;
			z = 0;
			leftAngle = 0;
			upAngle = 0;
			coins = 0;
			transmitter.transmit(new CharacterResumedMessage(new Vector3d(x, y, z), new EulerAngles(leftAngle, upAngle, 0)));
			sendCoinsUpdate();
		} else if (message instanceof UpdatePositionMessage) {
			final UpdatePositionMessage typedMessage = (UpdatePositionMessage)message;
			x = typedMessage.getPosition().getX();
			y = typedMessage.getPosition().getY();
			z = typedMessage.getPosition().getZ();
			leftAngle = typedMessage.getOrientation().getHorizontalAngle();
			upAngle = typedMessage.getOrientation().getVerticalAngle();
		} else if (message instanceof SectionDataRequestMessage) {
			SectionDataId sectionDataId = ((SectionDataRequestMessage)message).getSectionDataId();
			// don't give the clients definitive section data to prevent information cheating
			if (sectionDataId.getType() == SectionDataType.DEFINITIVE) {
				sectionDataId = new SectionDataId(sectionDataId.getSectionId(), SectionDataType.INTERACTIVE);
			}
			server.shipSectionData(this, sectionDataId);
		} else if (message instanceof SingleCubeModificationMessage) {
			SingleCubeModificationMessage typedMessage = (SingleCubeModificationMessage)message;
			server.setCube(typedMessage.getX(), typedMessage.getY(), typedMessage.getZ(), typedMessage.getNewCubeTypeIndex(), this);
		} else if (message instanceof BreakFreeMessage) {
			int centerX = (int)x;
			int centerY = (int)y;
			int centerZ = (int)z;
			for (int x = centerX - 2; x < centerX + 2; x++) {
				for (int y = centerY - 2; y < centerY + 2; y++) {
					for (int z = centerZ - 2; z < centerZ + 2; z++) {
						server.setCube(x, y, z, (byte)0, this);
					}
				}
			}
		} else if (message instanceof ConsoleCommandMessage) {
			final ConsoleCommandMessage typedMessage = (ConsoleCommandMessage)message;
			server.handleCommand(this, typedMessage.getCommand(), typedMessage.getArgs());
		} else {
			logger.error("unknown message from client: " + message);
		}
	}

}
