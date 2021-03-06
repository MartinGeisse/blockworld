/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.server.terrain;

import java.util.Random;
import org.apache.log4j.Logger;
import name.martingeisse.blockworld.common.cubes.Cubes;
import name.martingeisse.blockworld.common.geometry.GeometryConstants;
import name.martingeisse.blockworld.common.geometry.SectionId;
import name.martingeisse.blockworld.common.protocol.SectionDataId;
import name.martingeisse.blockworld.common.protocol.SectionDataType;
import name.martingeisse.blockworld.common.util.PerlinNoise;
import name.martingeisse.blockworld.common.util.task.ParallelGoal;
import name.martingeisse.blockworld.server.section.storage.AbstractSectionStorage;

/**
 * The main entry point to terrain generation.
 */
public final class TerrainGenerator {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(TerrainGenerator.class);

	/**
	 * the octaves
	 */
	private static final PerlinNoise[] heightFieldOctaves = new PerlinNoise[10];

	static {
		for (int i = 0; i < heightFieldOctaves.length; i++) {
			heightFieldOctaves[i] = new PerlinNoise(i);
		}
	}

	/**
	 * the snowNoise
	 */
	private static final PerlinNoise snowNoise = new PerlinNoise(123);

	/**
	 * the random
	 */
	private final Random random = new Random();

	/**
	 * the heightFieldAmplitude
	 */
	private double heightFieldAmplitude = 200.0;

	/**
	 * the heightFieldWavelength
	 */
	private double heightFieldWavelength = 200.0;

	/**
	 * the heightFieldOctaveCount
	 */
	private int heightFieldOctaveCount = heightFieldOctaves.length;

	/**
	 * the heightFieldOctaveFactor
	 */
	private double heightFieldOctaveFactor = 0.4;

	/**
	 * Constructor.
	 */
	public TerrainGenerator() {
	}

	/**
	 * Getter method for the random.
	 * @return the random
	 */
	public Random getRandom() {
		return random;
	}

	/**
	 * Getter method for the heightFieldAmplitude.
	 * @return the heightFieldAmplitude
	 */
	public double getHeightFieldAmplitude() {
		return heightFieldAmplitude;
	}

	/**
	 * Setter method for the heightFieldAmplitude.
	 * @param heightFieldAmplitude the heightFieldAmplitude to set
	 */
	public void setHeightFieldAmplitude(final double heightFieldAmplitude) {
		this.heightFieldAmplitude = heightFieldAmplitude;
	}

	/**
	 * Getter method for the heightFieldWavelength.
	 * @return the heightFieldWavelength
	 */
	public double getHeightFieldWavelength() {
		return heightFieldWavelength;
	}

	/**
	 * Setter method for the heightFieldWavelength.
	 * @param heightFieldWavelength the heightFieldWavelength to set
	 */
	public void setHeightFieldWavelength(final double heightFieldWavelength) {
		this.heightFieldWavelength = heightFieldWavelength;
	}

	/**
	 * Getter method for the heightFieldOctaveCount.
	 * @return the heightFieldOctaveCount
	 */
	public int getHeightFieldOctaveCount() {
		return heightFieldOctaveCount;
	}

	/**
	 * Setter method for the heightFieldOctaveCount.
	 * @param heightFieldOctaveCount the heightFieldOctaveCount to set
	 */
	public void setHeightFieldOctaveCount(final int heightFieldOctaveCount) {
		this.heightFieldOctaveCount = heightFieldOctaveCount;
	}

	/**
	 * Getter method for the heightFieldOctaveFactor.
	 * @return the heightFieldOctaveFactor
	 */
	public double getHeightFieldOctaveFactor() {
		return heightFieldOctaveFactor;
	}

	/**
	 * Setter method for the heightFieldOctaveFactor.
	 * @param heightFieldOctaveFactor the heightFieldOctaveFactor to set
	 */
	public void setHeightFieldOctaveFactor(final double heightFieldOctaveFactor) {
		this.heightFieldOctaveFactor = heightFieldOctaveFactor;
	}

	/**
	 * Generates terrain sections for a rectangular block of sections.
	 *
	 * @param storage the section storage
	 * @param min the section ID with lowest coordinates along all three axes (inclusive)
	 * @param max the section ID with highest coordinates along all three axes (inclusive)
	 */
	public void generate(final AbstractSectionStorage storage, final SectionId min, final SectionId max) {
		final ParallelGoal goal = new ParallelGoal();
		goal.schedule(); // TODO allow quick autostart without thread round-trip
		for (int x = min.getX(); x <= max.getX(); x++) {
			final int finalX = x;
			for (int z = min.getZ(); z <= max.getZ(); z++) {
				final int finalZ = z;
				goal.addSubgoal(() -> {
					generate(storage, finalX, min.getY(), max.getY(), finalZ);
				});
			}
		}
		goal.seal();
		try {
			goal.await();
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Generates terrain sections for a single section. Note that block-wise generation is usually faster.
	 *
	 * @param storage the section storage
	 * @param sectionId the ID of the section to generate
	 */
	public void generate(final AbstractSectionStorage storage, final SectionId sectionId) {
		generate(storage, sectionId.getX(), sectionId.getY(), sectionId.getY(), sectionId.getZ());
	}

	/**
	 * Generates terrain sections for a "column" of sections.
	 *
	 * @param storage the section storage
	 * @param sectionX the x component of section IDs to handle
	 * @param minSectionY the minimum Y component of section IDs to handle
	 * @param maxSectionY the maximum Y component of section IDs to handle
	 * @param sectionZ the z component of section IDs to handle
	 */
	public void generate(final AbstractSectionStorage storage, final int sectionX, final int minSectionY, final int maxSectionY, final int sectionZ) {
		logger.debug("generating random terrain for section column at " + sectionX + ", " + sectionZ);

		// some common values
		final int size = GeometryConstants.SECTION_SIZE;
		final int baseX = sectionX * size;
		final int baseZ = sectionZ * size;

		// generate height field
		final double[] heightField = new double[size * size];
		{
			double amplitude = heightFieldAmplitude;
			double wavelength = heightFieldWavelength;
			for (int octave = 0; octave < heightFieldOctaveCount; octave++) {
				for (int x = 0; x < size; x++) {
					for (int z = 0; z < size; z++) {
						final double contribution = heightFieldOctaves[octave].computeNoise((x + baseX) / wavelength, (z + baseZ) / wavelength) * amplitude;
						heightField[z * size + x] += contribution;
					}
				}
				amplitude *= heightFieldOctaveFactor;
				wavelength /= 2.0;
			}
		}
		final int[] intHeightField = new int[heightField.length];
		for (int i = 0; i < heightField.length; i++) {
			intHeightField[i] = (int)heightField[i];
		}
		logger.trace("height field generated");

		// generate snow delta field
		final double[] snowDeltaField = new double[size * size];
		{
			final double amplitude = 10.0;
			final double wavelength = 10.0;
			for (int x = 0; x < size; x++) {
				for (int z = 0; z < size; z++) {
					final double contribution = snowNoise.computeNoise((x + baseX) / wavelength, (z + baseZ) / wavelength) * amplitude;
					snowDeltaField[z * size + x] += contribution;
				}
			}
		}
		final int[] intSnowDeltaField = new int[snowDeltaField.length];
		for (int i = 0; i < snowDeltaField.length; i++) {
			intSnowDeltaField[i] = (int)snowDeltaField[i];
		}
		logger.trace("snow delta field generated");

		// generate sections
		for (int sectionYCounter = minSectionY; sectionYCounter <= maxSectionY; sectionYCounter++) {
			final int sectionY = sectionYCounter;
			final SectionId sectionId = new SectionId(sectionX, sectionY, sectionZ);
			final int baseY = sectionY * size;
			final byte[] cubes = new byte[size * size * size];
			final Editor editor = new Editor(cubes, size);

			// fill ocean cubes
			if (sectionY < 0) {
				for (int x = 0; x < size; x++) {
					for (int y = 0; y < size; y++) {
						for (int z = 0; z < size; z++) {
							editor.setCube(x, y, z, (byte)9);
						}
					}
				}
			}

			// make the base height field
			for (int x = 0; x < size; x++) {
				for (int z = 0; z < size; z++) {
					final int height = intHeightField[z * size + x];
					final int relativeHeight = height - baseY;
					final boolean shore = (height < 4);
					final int heightForSnow = height + intSnowDeltaField[z * size + x];
					final int snowHeight = (heightForSnow < 30 ? 0 : heightForSnow > 90 ? 3 : heightForSnow / 30);
					for (int y = 0; y < relativeHeight && y < size; y++) {
						editor.setCube(x, y, z, shore ? (byte)12 : (byte)3); // dirt or sand
					}
					for (int y = 0; y < relativeHeight - 3 && y < size; y++) {
						editor.setCube(x, y, z, (byte)1); // stone
					}
					if (snowHeight > 0) {
						for (int i = 0; i < snowHeight; i++) {
							if (relativeHeight - i > 0 && relativeHeight - i <= size) {
								editor.setCube(x, relativeHeight - i - 1, z, (byte)78); // snow
							}
						}
					} else {
						if (!shore && relativeHeight > 0 && relativeHeight <= size) {
							editor.setCube(x, relativeHeight - 1, z, (byte)2); // grass
						}
					}
				}
			}

			// place some tall grass
			final Random grassRandom = new Random(sectionX << 32 + sectionZ);
			for (int g = 0; g < 40; g++) {
				final int grassX = grassRandom.nextInt(size);
				final int grassZ = grassRandom.nextInt(size);
				final int grassY = intHeightField[grassZ * size + grassX];
				if (grassY >= 4 && grassY < 30) {
					editor.setCube(grassX, grassY - baseY, grassZ, (byte)31);
				}
			}

			// spread some coal
			for (int x = 0; x < size; x++) {
				for (int z = 0; z < size; z++) {
					final int y = random.nextInt(size);
					if (editor.getCube(x, y, z) == 1) {
						editor.setCube(x, y, z, (byte)16);
					}
				}
			}

			// spread some gold and gems
			final byte[] goldAndGems = {
				14,
				14,
				14
			};
			for (final byte cubeType : goldAndGems) {
				final int x = random.nextInt(size);
				final int y = random.nextInt(size);
				final int z = random.nextInt(size);
				if (editor.getCube(x, y, z) == 1) {
					editor.setCube(x, y, z, cubeType);
				}
			}

			// place trees within the section, but stay away from the boundaries so the tree doesn't overlap them
			final Random treeRandom = new Random(sectionX << 32 + sectionZ);
			for (int t = 0; t < 5; t++) {
				final int treeX = treeRandom.nextInt(size - 4) + 2;
				final int treeZ = treeRandom.nextInt(size - 4) + 2;
				final int treeRootHeight = intHeightField[treeZ * size + treeX];
				if (treeRootHeight > 1 && treeRootHeight < 50) {
					final int treeHeight = treeRandom.nextInt(5) + 5;

					// trunk
					for (int y = 0; y < treeHeight; y++) {
						editor.setCube(treeX, treeRootHeight - baseY + y, treeZ, (byte)17);
					}

					// leaves
					final int relativeTreeTopY = treeRootHeight - baseY + treeHeight;
					for (int dx = -1; dx <= 1; dx++) {
						for (int dy = -1; dy <= 1; dy++) {
							for (int dz = -1; dz <= 1; dz++) {
								editor.setCube(treeX + dx, relativeTreeTopY + dy, treeZ + dz, (byte)18);
							}
						}
					}
					editor.setCube(treeX - 2, relativeTreeTopY, treeZ, (byte)18);
					editor.setCube(treeX + 2, relativeTreeTopY, treeZ, (byte)18);
					editor.setCube(treeX, relativeTreeTopY + 2, treeZ, (byte)18);
					editor.setCube(treeX, relativeTreeTopY, treeZ - 2, (byte)18);
					editor.setCube(treeX, relativeTreeTopY, treeZ + 2, (byte)18);

				}
			}

			// compress section data and send it to storage
			final byte[] compressedCubes = Cubes.createFromCubes(cubes).compressToByteArray();
			storage.saveSectionRelatedObject(new SectionDataId(sectionId, SectionDataType.DEFINITIVE), compressedCubes);
		}
		logger.debug("random terrain generated for section column at " + sectionX + ", " + sectionZ);

	}

	private static class Editor {

		private final byte[] data;
		private final int size;

		Editor(final byte[] data, final int size) {
			this.data = data;
			this.size = size;
		}

		boolean contains(int x, int y, int z) {
			return (x >= 0 && y >= 0 && z >= 0 && x < size && y < size && z < size);
		}

		byte getCube(int x, int y, int z) {
			return (contains(x, y, z) ? data[x * size * size + y * size + z] : 0);
		}

		void setCube(int x, int y, int z, byte value) {
			if (contains(x, y, z)) {
				data[x * size * size + y * size + z] = value;
			}
		}
		
	}
}
