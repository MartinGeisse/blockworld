/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.ingame;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LESS;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_GEN_Q;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_GEN_R;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_GEN_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_GEN_T;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glVertex3i;
import static org.lwjgl.opengl.GL14.glWindowPos2i;
import static org.lwjgl.util.glu.GLU.gluPerspective;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import com.google.inject.Inject;
import name.martingeisse.blockworld.client.assets.MinerResources;
import name.martingeisse.blockworld.client.console.Console;
import name.martingeisse.blockworld.client.console.ConsoleUi;
import name.martingeisse.blockworld.client.engine.EngineParameters;
import name.martingeisse.blockworld.client.engine.FrameRenderParameters;
import name.martingeisse.blockworld.client.engine.WorldWorkingSet;
import name.martingeisse.blockworld.client.engine.renderer.DefaultSectionRenderer;
import name.martingeisse.blockworld.client.glworker.GlWorkUnit;
import name.martingeisse.blockworld.client.glworker.GlWorkerLoop;
import name.martingeisse.blockworld.client.gui.base.Gui;
import name.martingeisse.blockworld.client.gui.base.GuiFrameHandler;
import name.martingeisse.blockworld.client.gui.ingame.MainMenuPage;
import name.martingeisse.blockworld.client.hud.FlashMessagePanel;
import name.martingeisse.blockworld.client.hud.Hud;
import name.martingeisse.blockworld.client.hud.SelectedCubePanel;
import name.martingeisse.blockworld.client.ingame.player.Player;
import name.martingeisse.blockworld.client.ingame.player.PlayerProxy;
import name.martingeisse.blockworld.client.network.ClientToServerTransmitter;
import name.martingeisse.blockworld.client.network.ServerToClientReceiver;
import name.martingeisse.blockworld.client.shell.BreakFrameLoopException;
import name.martingeisse.blockworld.client.shell.FrameHandler;
import name.martingeisse.blockworld.client.util.lwjgl.MouseUtil;
import name.martingeisse.blockworld.client.util.lwjgl.RayAction;
import name.martingeisse.blockworld.client.util.lwjgl.RayActionSupport;
import name.martingeisse.blockworld.client.util.lwjgl.RegularSound;
import name.martingeisse.blockworld.client.util.resource.Font;
import name.martingeisse.blockworld.common.cubetype.MinerCubeTypes;
import name.martingeisse.blockworld.common.geometry.AxisAlignedDirection;
import name.martingeisse.blockworld.common.geometry.GeometryConstants;
import name.martingeisse.blockworld.common.network.c2s_message.BreakFreeMessage;
import name.martingeisse.blockworld.common.network.c2s_message.ResumeCharacterMessage;
import name.martingeisse.blockworld.common.network.c2s_message.SingleCubeModificationMessage;
import name.martingeisse.blockworld.common.network.c2s_message.UpdatePositionMessage;
import name.martingeisse.blockworld.common.network.s2c_message.CharacterResumedMessage;
import name.martingeisse.blockworld.common.network.s2c_message.ConsoleOutputMessage;
import name.martingeisse.blockworld.common.network.s2c_message.FlashMessage;
import name.martingeisse.blockworld.common.network.s2c_message.OtherCharactersUpdateMessage;
import name.martingeisse.blockworld.common.network.s2c_message.SectionDataResponseMessage;
import name.martingeisse.blockworld.common.network.s2c_message.ServerToClientMessage;
import name.martingeisse.blockworld.common.network.s2c_message.SingleSectionModificationMessage;
import name.martingeisse.blockworld.common.network.s2c_message.UpdateCoinsMessage;
import name.martingeisse.blockworld.common.util.IntervalInvoker;
import name.martingeisse.blockworld.common.util.ProfilingHelper;
import name.martingeisse.blockworld.geometry.EulerAngles;
import name.martingeisse.blockworld.geometry.MutableEulerAngles;
import name.martingeisse.blockworld.geometry.MutableVector3d;
import name.martingeisse.blockworld.geometry.Vector3d;

/**
 *
 */
public class IngameFrameHandler implements FrameHandler {

	private static Logger logger = Logger.getLogger(IngameFrameHandler.class);

	/**
	 * The maximum height the player can climb without jumping.
	 */
	public static final double MAX_STAIRS_HEIGHT = 0.8;

	private final Hud hud;
	private final SelectedCubePanel selectedCubePanel;
	private final ConsoleUi consoleUi;
	private final Console console;
	private final IntervalInvoker sendPlayerPositionInvoker;
	private final ClientToServerTransmitter clientToServerTransmitter;
	private final ServerToClientReceiver serverToClientReceiver;
	private final FrameHandler menuFrameHandler;
	private final FlashMessagePanel flashMessagePanel;

	private boolean menuActive;
	private boolean closeRequested;
	private Player player;
	private WorldWorkingSet workingSet;
	private boolean infoButtonPressed;
	private RayActionSupport rayActionSupport;
	private boolean captureRayActionSupport;
	private boolean wireframe;
	private boolean grid;
	private byte currentCubeType = 1;
	private List<PlayerProxy> playerProxies;
	private RegularSound footstepSound;
	private boolean walking;
	private long cooldownFinishTime;
	private Instant previousConnectionProblemInstant = new Instant();
	private OtherPlayerVisualTemplate otherPlayerVisualTemplate;
	private int sectionLoadRequestCooldown = 0;
	private IntervalInvoker sectionLoadHandler;
	private long coins;
	private SectionGridLoader sectionGridLoader;
	private boolean consoleVisible;
	private boolean consolePreviouslyVisible;

	/**
	 * Constructor.
	 * @param hud (injected)
	 * @param selectedCubePanel (injected)
	 * @param consoleUi (injected)
	 * @param clientToServerTransmitter (injected)
	 * @param serverToClientReceiver (injected)
	 * @param flashMessagePanel (injected)
	 * @param console (injected)
	 */
	@Inject
	public IngameFrameHandler(final Hud hud, final SelectedCubePanel selectedCubePanel, final ConsoleUi consoleUi, final ClientToServerTransmitter clientToServerTransmitter, final ServerToClientReceiver serverToClientReceiver, final FlashMessagePanel flashMessagePanel, final Console console) {
		this.hud = hud;
		this.selectedCubePanel = selectedCubePanel;
		this.consoleUi = consoleUi;
		this.console = console;
		this.clientToServerTransmitter = clientToServerTransmitter;
		this.serverToClientReceiver = serverToClientReceiver;
		this.flashMessagePanel = flashMessagePanel;
		this.sendPlayerPositionInvoker = new IntervalInvoker(200, () -> {
			final MutableVector3d mutablePosition = player.getPosition();
			final Vector3d position = new Vector3d(mutablePosition.x, mutablePosition.y, mutablePosition.z);
			final MutableEulerAngles mutableOrientation = player.getOrientation();
			final EulerAngles orientation = new EulerAngles(mutableOrientation.getHorizontalAngle(), mutableOrientation.getVerticalAngle(), mutableOrientation.getRollAngle());
			clientToServerTransmitter.transmit(new UpdatePositionMessage(position, orientation));
		});
		this.menuFrameHandler = new GuiFrameHandler() {

			@Override
			protected void configureGui(final Gui gui) {
				gui.setDefaultFont(MinerResources.getInstance().getFont());
				gui.setRootElement(new MainMenuPage() {

					// override
					@Override
					protected void resumeGame() {
						menuActive = false;
					}

					// override
					@Override
					protected void quitGame() {
						closeRequested = true;
					}

				});
			}
		};
		this.consoleVisible = false;
		this.consolePreviouslyVisible = false;

		/**
		 * The sectionLoadHandler -- checks often (100 ms), but doesn't re-request frequently (5 sec)
		 * to avoid re-requesting a section again and again while the server is loading it.
		 *
		 * TODO should be resettable for edge cases where frequent reloading is needed, such as
		 * falling down from high places.
		 * This handler checks if sections must be loaded.
		 */
		sectionLoadHandler = new IntervalInvoker(100, () -> {
			if (sectionLoadRequestCooldown == 0) {
				sectionGridLoader.setViewerPosition(player.getSectionId());
				if (sectionGridLoader.update()) {
					sectionLoadRequestCooldown = 50;
				}
			} else {
				sectionLoadRequestCooldown--;
			}
		});

	}

	/**
	 *
	 */
	public void initialize() {

		// the world
		final DefaultSectionRenderer sectionRenderer = new DefaultSectionRenderer();
		sectionRenderer.prepareForTextures(MinerResources.getInstance().getCubeTextures());
		final EngineParameters engineParameters = new EngineParameters(sectionRenderer, MinerResources.getInstance().getCubeTextures(), MinerCubeTypes.CUBE_TYPES);
		workingSet = new WorldWorkingSet(engineParameters, GeometryConstants.SECTION_CLUSTER_SIZE);

		// the player
		player = new Player(workingSet);
		player.getPosition().setX(0);
		player.getPosition().setY(10);
		player.getPosition().setZ(0);

		// other stuff
		rayActionSupport = new RayActionSupport();
		playerProxies = new ArrayList<PlayerProxy>();
		footstepSound = new RegularSound(MinerResources.getInstance().getFootstep(), 500);
		cooldownFinishTime = System.currentTimeMillis();
		otherPlayerVisualTemplate = new OtherPlayerVisualTemplate(player);

		// TODO: implement better checking for connection problems: only stall when surrounding sections
		// are missing AND the player is in that half of the current section. currently using collider
		// radius 2 to avoid "connection problems" when crossing a section boundary
		sectionGridLoader = new SectionGridLoader(workingSet, clientToServerTransmitter, 3, 2);

	}

	/**
	 * Resumes a character using the specified token.
	 *
	 * @param playCharacterToken the token
	 */
	public void resumePlayer(final String playCharacterToken) {
		clientToServerTransmitter.transmit(new ResumeCharacterMessage(playCharacterToken));
	}

	// override
	@Override
	public void step() throws BreakFrameLoopException {

		// handle quitting the game
		if (Display.isCloseRequested() || Keyboard.isKeyDown(Keyboard.KEY_F10) || closeRequested) {
			clientToServerTransmitter.disconnect();
			throw new BreakFrameLoopException();
		}

		// handle network messages
		while (true) {
			final ServerToClientMessage message = serverToClientReceiver.poll();
			if (message == null) {
				break;
			}
			if (message instanceof CharacterResumedMessage) {
				final CharacterResumedMessage typedMessage = (CharacterResumedMessage)message;
				player.getPosition().copyFrom(typedMessage.getPosition());
				player.getOrientation().copyFrom(typedMessage.getOrientation());
				sectionGridLoader.setViewerPosition(player.getSectionId());
			} else if (message instanceof SectionDataResponseMessage) {
				sectionGridLoader.handleSectionDataResponse((SectionDataResponseMessage)message);
			} else if (message instanceof SingleSectionModificationMessage) {
				sectionGridLoader.handleModificationEventPacket((SingleSectionModificationMessage)message);
			} else if (message instanceof OtherCharactersUpdateMessage) {
				playerProxies = new ArrayList<>();
				for (final OtherCharactersUpdateMessage.Entry entry : ((OtherCharactersUpdateMessage)message).getEntries()) {
					final PlayerProxy playerProxy = new PlayerProxy(entry.getName());
					playerProxy.getPosition().copyFrom(entry.getPosition());
					playerProxy.getOrientation().copyFrom(entry.getOrientation());
					playerProxies.add(playerProxy);
				}
			} else if (message instanceof FlashMessage) {
				flashMessagePanel.addMessage(((FlashMessage)message).getText());
			} else if (message instanceof UpdateCoinsMessage) {
				coins = ((UpdateCoinsMessage)message).getCoins();
			} else if (message instanceof ConsoleOutputMessage) {
				for (final String line : ((ConsoleOutputMessage)message).getLines()) {
					console.println(line);
				}
			} else {
				logger.error("unknown message: " + message);
			}
		}

		if (menuActive) {
			
			// TODO properly disable all keyboard / mouse handling in the CubeWorldHandler when the GUI is active
			menuFrameHandler.step();
			
		} else {
			
			// handle menu key
			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				menuActive = true;
				MouseUtil.ungrab();
			}
			
			// handle console visibility
			// TODO KEY_SECTION only makes sense on a German Mac keyboard
			if (Keyboard.isKeyDown(Keyboard.KEY_SECTION)) {
				if (consoleVisible == consolePreviouslyVisible) {
					consoleVisible = !consoleVisible;
				}
			} else {
				consolePreviouslyVisible = consoleVisible;
			}

			// redirect input to the console if it is visible, otherwise drop them
			if (consoleVisible) {
				consoleUi.consumeKeyboardEvents();
			} else {
				while (Keyboard.next());
			}
			
		}

		// first, handle the stuff that already works without the world being loaded "enough"
		if (!menuActive && Keyboard.isKeyDown(Keyboard.KEY_I)) {
			if (!infoButtonPressed) {
				player.dump();
			}
			infoButtonPressed = true;
		} else {
			infoButtonPressed = false;
		}
		if (!menuActive && Keyboard.isKeyDown(Keyboard.KEY_P)) {
			player.setObserverMode(false);
		}
		if (!menuActive && Keyboard.isKeyDown(Keyboard.KEY_O)) {
			player.setObserverMode(true);
		}
		if (!menuActive && Keyboard.isKeyDown(Keyboard.KEY_G)) {
			grid = true;
		}
		if (!menuActive && Keyboard.isKeyDown(Keyboard.KEY_H)) {
			grid = false;
		}
		if (!menuActive && Keyboard.isKeyDown(Keyboard.KEY_K)) {
			MouseUtil.ungrab();
		}
		if (!menuActive && Keyboard.isKeyDown(Keyboard.KEY_L)) {
			MouseUtil.grab();
		}
		wireframe = !menuActive && Keyboard.isKeyDown(Keyboard.KEY_F);
		player.setWantsToJump(!menuActive && Keyboard.isKeyDown(Keyboard.KEY_SPACE));
		if (!menuActive && Keyboard.isKeyDown(Keyboard.KEY_1)) {
			currentCubeType = 1;
		} else if (!menuActive && Keyboard.isKeyDown(Keyboard.KEY_2)) {
			currentCubeType = 2;
		} else if (!menuActive && Keyboard.isKeyDown(Keyboard.KEY_3)) {
			currentCubeType = 3;
		} else if (!menuActive && Keyboard.isKeyDown(Keyboard.KEY_4)) {
			currentCubeType = 50;
		}
		if (!menuActive) {
			player.getOrientation().setHorizontalAngle(player.getOrientation().getHorizontalAngle() - Mouse.getDX() * 0.5);
			double newUpAngle = player.getOrientation().getVerticalAngle() + Mouse.getDY() * 0.5;
			newUpAngle = (newUpAngle > 90) ? 90 : (newUpAngle < -90) ? -90 : newUpAngle;
			player.getOrientation().setVerticalAngle(newUpAngle);
		}
		sectionLoadHandler.step();

		// check if the world is loaded "enough"
		workingSet.acceptLoadedSections();
		if (!workingSet.hasAllRenderModels(player.getSectionId(), 1) || !workingSet.hasAllColliders(player.getSectionId(), 1)) {
			final Instant now = new Instant();
			if (new Duration(previousConnectionProblemInstant, now).getMillis() >= 1000) {
				// logger.warn("connection problems");
				previousConnectionProblemInstant = now;
			}
			return;
		}

		// ---------------------------------------------------------------------------------------------------
		// now, handle the stuff that only works with enough information from the world
		// ---------------------------------------------------------------------------------------------------

		// normal movement: If on the ground, we move the player step-up, then front/side, then step-down.
		// This way the player can climb stairs while walking. In the air, this boils down to front/side movement.
		// We also keep track if the player is walking (front/side) for a "walking" sound effect.
		double speed = !menuActive && Keyboard.isKeyDown(Keyboard.KEY_TAB) ? 10.0 : !menuActive && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? 3.0 : 1.5;
		speed *= 0.1;
		walking = false;
		double forward = 0, right = 0;
		if (!menuActive && Keyboard.isKeyDown(Keyboard.KEY_A)) {
			right = -speed;
			walking = true;
		}
		if (!menuActive && Keyboard.isKeyDown(Keyboard.KEY_D)) {
			right = speed;
			walking = true;
		}
		if (!menuActive && Keyboard.isKeyDown(Keyboard.KEY_W)) {
			forward = speed;
			walking = true;
		}
		if (!menuActive && Keyboard.isKeyDown(Keyboard.KEY_S)) {
			forward = -speed;
			walking = true;
		}
		player.moveHorizontal(forward, right, player.isOnGround() ? MAX_STAIRS_HEIGHT : 0);

		// special movement
		if (!menuActive && Keyboard.isKeyDown(Keyboard.KEY_C)) {
			player.moveUp(-speed);
		}
		if (!menuActive && Keyboard.isKeyDown(Keyboard.KEY_E)) {
			player.moveUp(speed);
		}

		// cube placement
		captureRayActionSupport = false;
		final long now = System.currentTimeMillis();
		if (now >= cooldownFinishTime) {
			if (!menuActive && Mouse.isButtonDown(0)) {
				captureRayActionSupport = true;
				rayActionSupport.execute(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), new RayAction(false) {

					@Override
					public void handleImpact(final int x, final int y, final int z, final double distance) {
						if (distance < 3.0 && !player.createCollisionRegion().contains(x, y, z)) {
							final byte effectiveCubeType;
							if (currentCubeType == 50) {
								final int angle = ((int)player.getOrientation().getHorizontalAngle() % 360 + 360) % 360;
								if (angle < 45) {
									effectiveCubeType = 52;
								} else if (angle < 45 + 90) {
									effectiveCubeType = 50;
								} else if (angle < 45 + 180) {
									effectiveCubeType = 53;
								} else if (angle < 45 + 270) {
									effectiveCubeType = 51;
								} else {
									effectiveCubeType = 52;
								}
							} else {
								effectiveCubeType = currentCubeType;
							}
							clientToServerTransmitter.transmit(new SingleCubeModificationMessage(x, y, z, effectiveCubeType));
							cooldownFinishTime = now + 200;
						}
					}
				});
			} else if (!menuActive && Mouse.isButtonDown(1)) {
				captureRayActionSupport = true;
				rayActionSupport.execute(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), new RayAction(true) {

					@Override
					public void handleImpact(final int x, final int y, final int z, final double distance) {
						if (distance < 2.0) {
							clientToServerTransmitter.transmit(new SingleCubeModificationMessage(x, y, z, (byte)0));
							MinerResources.getInstance().getHitCube().playAsSoundEffect(1.0f, 1.0f, false);
							cooldownFinishTime = now + 200;
						}
					}
				});
			}
		}

		// special actions
		if (!menuActive && Keyboard.isKeyDown(Keyboard.KEY_B)) {
			clientToServerTransmitter.transmit(new BreakFreeMessage(player.createCollisionRegion()));
		}

		// handle player logic
		player.step(0.1);

		// handle sound effects
		if (player.isOnGround() && walking) {
			footstepSound.handleActiveTime();
		} else {
			footstepSound.reset();
		}
		if (player.isJustLanded()) {
			MinerResources.getInstance().getLandOnGround().playAsSoundEffect(1.0f, 1.0f, false);
		}

		sendPlayerPositionInvoker.step();
	}

	// override
	@Override
	public void draw() {

		// determine player's position as integers
		final int playerX = (int)(Math.floor(player.getPosition().getX()));
		final int playerY = (int)(Math.floor(player.getPosition().getY()));
		final int playerZ = (int)(Math.floor(player.getPosition().getZ()));

		// run preparation code in the OpenGL worker thread
		GlWorkerLoop.getInstance().schedule(new GlWorkUnit() {

			@Override
			public void execute() {

				// profiling
				ProfilingHelper.start();

				// set up projection matrix
				glMatrixMode(GL_PROJECTION);
				glLoadIdentity();
				gluPerspective(60, (float)Display.getWidth() / (float)Display.getHeight(), 0.1f, 10000.0f);

				// set up modelview matrix
				glMatrixMode(GL_MODELVIEW);
				glLoadIdentity(); // model transformation (direct)
				glRotatef((float)player.getOrientation().getVerticalAngle(), -1, 0, 0); // view transformation (reversed)
				glRotatef((float)player.getOrientation().getHorizontalAngle(), 0, -1, 0); // ...
				glTranslated(-player.getPosition().getX(), -player.getPosition().getY(), -player.getPosition().getZ()); // ...

				// clear the screen
				glDepthMask(true);
				glClearColor(0.5f, 0.5f, 1.0f, 1.0f);
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

				// some more preparation
				glDepthFunc(GL_LESS);
				glEnable(GL_DEPTH_TEST);
				((DefaultSectionRenderer)workingSet.getEngineParameters().getSectionRenderer()).setWireframe(wireframe);
				((DefaultSectionRenderer)workingSet.getEngineParameters().getSectionRenderer()).setTexturing(true);
				((DefaultSectionRenderer)workingSet.getEngineParameters().getSectionRenderer()).setTextureCoordinateGeneration(true);

				// scale by the inverse detail factor for drawing the cubes, but prepare for scaling back
				glPushMatrix();
				final float inverseFactor = 1.0f / GeometryConstants.GEOMETRY_DETAIL_FACTOR;
				glScalef(inverseFactor, inverseFactor, inverseFactor);

			}
		});

		// actually draw the world
		workingSet.draw(new FrameRenderParameters(playerX, playerY, playerZ));

		// post-draw code, again in the GL worker thread
		GlWorkerLoop.getInstance().schedule(new GlWorkUnit() {

			@Override
			public void execute() {

				// scale back for the remaining operations
				glPopMatrix();

				// Measure visible distance in the center of the crosshair, with only the world visible (no HUD or similar).
				// Only call if needed, this stalls the rendering pipeline --> 2x frame rate possible!
				if (captureRayActionSupport) {
					rayActionSupport.capture();
				} else {
					rayActionSupport.release();
				}

				// draw the sky
				glDisable(GL_TEXTURE_GEN_S);
				glDisable(GL_TEXTURE_GEN_T);
				glDisable(GL_TEXTURE_GEN_Q);
				glDisable(GL_TEXTURE_GEN_R);
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				glEnable(GL_TEXTURE_2D);
				MinerResources.getInstance().getClouds().glBindTexture();
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
				final float tex = 10.0f;
				glColor3f(1.0f, 1.0f, 1.0f);
				glBegin(GL_QUADS);
				glTexCoord2f(0, 0);
				glVertex3i(-100000, 1000, -100000);
				glTexCoord2f(tex, 0);
				glVertex3i(+100000, 1000, -100000);
				glTexCoord2f(tex, tex);
				glVertex3i(+100000, 1000, +100000);
				glTexCoord2f(0, tex);
				glVertex3i(-100000, 1000, +100000);
				glEnd();
				glDisable(GL_BLEND);

				// draw the grid
				if (grid) {
					glDisable(GL_TEXTURE_2D);
					glColor3f(1.0f, 1.0f, 1.0f);
					final int sectionX = playerX >> GeometryConstants.SECTION_SHIFT;
					final int sectionY = playerY >> GeometryConstants.SECTION_SHIFT;
					final int sectionZ = playerZ >> GeometryConstants.SECTION_SHIFT;
					final int distance = 48;
					glLineWidth(2.0f);
					glBegin(GL_LINES);
					for (int u = -3; u <= 4; u++) {
						for (int v = -3; v <= 4; v++) {
							for (final AxisAlignedDirection direction : AxisAlignedDirection.values()) {
								if (direction.isNegative()) {
									continue;
								}
								final int x = GeometryConstants.SECTION_SIZE * (sectionX + direction.selectByAxis(0, u, v));
								final int dx = direction.selectByAxis(distance, 0, 0);
								final int y = GeometryConstants.SECTION_SIZE * (sectionY + direction.selectByAxis(v, 0, u));
								final int dy = direction.selectByAxis(0, distance, 0);
								final int z = GeometryConstants.SECTION_SIZE * (sectionZ + direction.selectByAxis(u, v, 0));
								final int dz = direction.selectByAxis(0, 0, distance);
								glVertex3f(x + dx, y + dy, z + dz);
								glVertex3f(x - dx, y - dy, z - dz);
							}
						}
					}
					glEnd();
				}

				// draw player proxies (i.e. other players)
				glBindTexture(GL_TEXTURE_2D, 0);
				glEnable(GL_BLEND);
				glMatrixMode(GL_MODELVIEW);
				for (final PlayerProxy playerProxy : playerProxies) {
					otherPlayerVisualTemplate.renderEmbedded(playerProxy);
				}
				glDisable(GL_BLEND);

				// draw the crosshair
				glLineWidth(1.0f);
				glMatrixMode(GL_PROJECTION);
				glLoadIdentity();
				glMatrixMode(GL_MODELVIEW);
				glLoadIdentity();
				glDisable(GL_DEPTH_TEST);
				glDisable(GL_TEXTURE_2D);
				glColor3f(1.0f, 1.0f, 1.0f);
				glBegin(GL_LINES);
				glVertex2f(-0.1f, 0.0f);
				glVertex2f(+0.1f, 0.0f);
				glVertex2f(0.0f, -0.1f);
				glVertex2f(0.0f, +0.1f);
				glEnd();

				// draw custom HUD elements
				glBindTexture(GL_TEXTURE_2D, 0);
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				glWindowPos2i(Display.getWidth(), Display.getHeight() - 30);
				GL11.glPixelTransferf(GL11.GL_RED_BIAS, 1.0f);
				GL11.glPixelTransferf(GL11.GL_GREEN_BIAS, 1.0f);
				GL11.glPixelTransferf(GL11.GL_BLUE_BIAS, 1.0f);
				MinerResources.getInstance().getFont().drawText("coins: " + coins, 2, Font.ALIGN_RIGHT, Font.ALIGN_TOP);
				GL11.glPixelTransferf(GL11.GL_RED_BIAS, 0.0f);
				GL11.glPixelTransferf(GL11.GL_GREEN_BIAS, 0.0f);
				GL11.glPixelTransferf(GL11.GL_BLUE_BIAS, 0.0f);

				// profiling
				ProfilingHelper.checkRelevant("draw", 50);

			}
		});

		// draw the main HUD
		selectedCubePanel.setCubeTypeIndex(currentCubeType);
		hud.draw();

		// draw the console
		if (consoleVisible) {
			consoleUi.draw();
		}

		// draw the menu
		if (menuActive) {
			menuFrameHandler.draw();
		}

	}

}
