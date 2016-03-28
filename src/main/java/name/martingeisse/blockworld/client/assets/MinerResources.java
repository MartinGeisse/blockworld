/**
 * Copyright (c) 2012 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.assets;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ClasspathLocation;
import org.newdawn.slick.util.ResourceLoader;
import name.martingeisse.blockworld.client.util.resource.FixedWidthFont;
import name.martingeisse.blockworld.client.util.resource.Font;
import name.martingeisse.blockworld.client.util.resource.Texture;
import name.martingeisse.blockworld.common.cubetype.MinerCubeTypes;

/**
 *
 */
public class MinerResources {

	/**
	 * the KEY_COLOR
	 */
	public static final int[] KEY_COLOR = {
		255,
		0,
		255
	};

	/**
	 * the instance
	 */
	private static MinerResources instance;

	/**
	 * Initializes the instance of this class and loads all resources.
	 * @throws IOException on I/O errors
	 */
	public synchronized static void initializeInstance() throws IOException {
		instance = new MinerResources();
	}

	/**
	 * Getter method for the instance.
	 * @return the instance
	 */
	public static MinerResources getInstance() {
		return instance;
	}

	/**
	 * the cubeTextures
	 */
	private final Texture[] cubeTextures;

	/**
	 * the clouds
	 */
	private final Texture clouds;

	/**
	 * the font
	 */
	private final Font font;

	/**
	 * the footstep
	 */
	private final Audio footstep;

	/**
	 * the hitCube
	 */
	private final Audio hitCube;

	/**
	 * the landOnGround
	 */
	private final Audio landOnGround;

	/**
	 * Constructor. The resources are immediately loaded.
	 * @throws IOException on I/O errors
	 */
	private MinerResources() throws IOException {

		// load cube textures
		final String[] cubeTextureNames = MinerCubeTypes.CUBE_TEXTURE_FILENAMES;
		cubeTextures = new Texture[cubeTextureNames.length];
		for (int i = 0; i < cubeTextures.length; i++) {
			cubeTextures[i] = new Texture(MinerResources.class, cubeTextureNames[i], false);
		}

		// load special textures
		clouds = new Texture(MinerResources.class, "clouds.png", false);
		font = new FixedWidthFont(loadImage("font.png"), 8, 16);

		// load sounds
		ResourceLoader.addResourceLocation(new ClasspathLocation());
		footstep = loadOggSound("footstep-1.ogg");
		hitCube = loadOggSound("hit-cube-1.ogg");
		landOnGround = loadOggSound("land.ogg");

	}

	/**
	 * @param filename the filename of the OGG, relative to the assets folder
	 * @return the sound
	 * @throws IOException on I/O errors
	 */
	private Audio loadOggSound(final String filename) throws IOException {
		try (InputStream inputStream = MinerResources.class.getResourceAsStream(filename)) {
			return AudioLoader.getAudio("OGG", inputStream);
		}
	}

	/**
	 * Loads a PNG image the AWT way.
	 * @param filename the filename of the PNG, relative to the assets folder
	 * @return the luminance buffer
	 * @throws IOException on I/O errors
	 */
	private BufferedImage loadImage(final String filename) throws IOException {
		try (InputStream inputStream = MinerResources.class.getResourceAsStream(filename)) {
			return ImageIO.read(inputStream);
		}
	}

	/**
	 * Getter method for the cubeTextures.
	 * @return the cubeTextures
	 */
	public Texture[] getCubeTextures() {
		return cubeTextures;
	}

	/**
	 * Getter method for the clouds.
	 * @return the clouds
	 */
	public Texture getClouds() {
		return clouds;
	}

	/**
	 * Getter method for the font.
	 * @return the font
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * Getter method for the footstep.
	 * @return the footstep
	 */
	public Audio getFootstep() {
		return footstep;
	}

	/**
	 * Getter method for the hitCube.
	 * @return the hitCube
	 */
	public Audio getHitCube() {
		return hitCube;
	}

	/**
	 * Getter method for the landOnGround.
	 * @return the landOnGround
	 */
	public Audio getLandOnGround() {
		return landOnGround;
	}

}
