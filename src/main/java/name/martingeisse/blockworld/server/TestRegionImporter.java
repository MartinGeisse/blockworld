/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.server;

import java.io.File;
import org.apache.commons.lang3.StringUtils;
import name.martingeisse.blockworld.common.cubes.Cubes;
import name.martingeisse.blockworld.common.cubes.RawCubes;
import name.martingeisse.blockworld.common.cubes.UniformCubes;
import name.martingeisse.blockworld.common.geometry.GeometryConstants;
import name.martingeisse.blockworld.common.geometry.SectionId;
import name.martingeisse.blockworld.common.protocol.SectionDataId;
import name.martingeisse.blockworld.common.protocol.SectionDataType;
import name.martingeisse.blockworld.server.section.storage.AbstractSectionStorage;
import name.martingeisse.blockworld.server.util.NbtParser;
import name.martingeisse.blockworld.server.util.RegionParser;

/**
 * Test to import an existing Minecraft map directly into the running server
 */
public final class TestRegionImporter {

	/**
	 * the storage
	 */
	private AbstractSectionStorage storage;
	
	/**
	 * the nbtParser
	 */
	private MyNbtParser nbtParser = new MyNbtParser();

	/**
	 * the regionParser
	 */
	private MyRegionParser regionParser = new MyRegionParser();
	
	/**
	 * the baseChunkX
	 */
	private int baseChunkX;
	
	/**
	 * the baseChunkZ
	 */
	private int baseChunkZ;
	
	/**
	 * the currentChunkX
	 */
	private int currentChunkX;
	
	/**
	 * the currentChunkZ
	 */
	private int currentChunkZ;
	
	/**
	 * the inSections
	 */
	private boolean inSections;
	
	/**
	 * the currentSectionY
	 */
	private int currentSectionY;
	
	/**
	 * the translateX
	 */
	private int translateX;

	/**
	 * the translateY
	 */
	private int translateY;

	/**
	 * the translateZ
	 */
	private int translateZ;

	/**
	 * Constructor.
	 * @param storage the section storage
	 */
	public TestRegionImporter(AbstractSectionStorage storage) {
		this.storage = storage;
	}
	
	/**
	 * Sets the translation in section-sized units.
	 * @param x the x translation
	 * @param y the y translation
	 * @param z the z translation
	 */
	public void setTranslation(int x, int y, int z) {
		this.translateX = x;
		this.translateY = y;
		this.translateZ = z;
	}
	
	/**
	 * Imports regions from the specified region folder.
	 * @param regionFolder the region folder
	 * @throws Exception on errors
	 */
	public void importRegions(File regionFolder) throws Exception {
		for (File regionFile : regionFolder.listFiles()) {
			handleRegionFile(regionFile);
		}
	}

	private void handleRegionFile(File regionFile) throws Exception {
		String[] segments = StringUtils.split(regionFile.getName(), '.');
		if (segments.length != 4) {
			return;
		}
		baseChunkX = Integer.parseInt(segments[1]) * 32;
		baseChunkZ = Integer.parseInt(segments[2]) * 32;
		regionParser.parse(regionFile);
	}
	
	/**
	 * 
	 */
	private class MyRegionParser extends RegionParser {
		
		MyRegionParser() {
			super(nbtParser);
		}
		
		// override
		@Override
		protected void onBeforeChunk(int x, int z) {
			currentChunkX = baseChunkX + x;
			currentChunkZ = baseChunkZ + z;
		}
		
		// override
		@Override
		protected void onAfterChunk(int x, int z) {
		}
		
		// override
		@Override
		protected void onEmptyChunk(int x, int z) {
		}
		
	}

	/**
	 * 
	 */
	private class MyNbtParser extends NbtParser {

		// override
		@Override
		protected void handleListStart(String tagName, int elementCount) {
			if (tagName.equals("Sections")) {
				inSections = true;
			}
		}
		
		/* (non-Javadoc)
		 * @see name.martingeisse.stackd.util.NbtParser#handleListEnd(java.lang.String, int)
		 */
		@Override
		protected void handleListEnd(String tagName, int elementCount) {
			if (tagName.equals("Sections")) {
				inSections = false;
			}
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.stackd.util.NbtParser#handleByte(java.lang.String, byte)
		 */
		@Override
		protected void handleByte(String tagName, byte value) {
			if (inSections && tagName.equals("Y")) {
				currentSectionY = value;
			}
		}
		
		/* (non-Javadoc)
		 * @see name.martingeisse.stackd.util.NbtParser#handleByteArray(java.lang.String, byte[])
		 */
		@Override
		protected void handleByteArray(String tagName, byte[] value) {
			if (inSections && tagName.equals("Blocks")) {
				if (value.length != 16 * 16 * 16) {
					throw new RuntimeException("Blocks tag with invalid size: " + value.length);
				}
				int originalSectionX = currentChunkX + translateX;
				int originalSectionY = currentSectionY + translateY;
				int originalSectionZ = currentChunkZ + translateZ;
				int modifiedShift = GeometryConstants.SECTION_SHIFT;
				int deltaShift = (modifiedShift - 4);
				int modifiedSectionX = originalSectionX >> deltaShift;
				int modifiedSectionY = originalSectionY >> deltaShift;
				int modifiedSectionZ = originalSectionZ >> deltaShift;
				int innerOffsetX = (originalSectionX & ((1 << deltaShift) - 1)) << 4;
				int innerOffsetY = (originalSectionY & ((1 << deltaShift) - 1)) << 4;
				int innerOffsetZ = (originalSectionZ & ((1 << deltaShift) - 1)) << 4;
				SectionId modifiedSectionId = new SectionId(modifiedSectionX, modifiedSectionY, modifiedSectionZ);
				SectionDataId sectionDataId = new SectionDataId(modifiedSectionId, SectionDataType.DEFINITIVE);
				byte[] loadedData = storage.loadSectionRelatedObject(sectionDataId);
				RawCubes data = (loadedData == null ? new UniformCubes((byte)0) : Cubes.createFromCompressedData(GeometryConstants.SECTION_CLUSTER_SIZE, loadedData)).convertToRawCubes(GeometryConstants.SECTION_CLUSTER_SIZE);
				byte[] cubes = data.getCubes();
				for (int y = 0; y < 16; y++) {
					int sourceBaseY = y * 16 * 16;
					for (int z = 0; z < 16; z++) {
						int sourceBaseYZ = sourceBaseY + z * 16;
						for (int x = 0; x < 16; x++) {
							int sourceIndex = sourceBaseYZ + x;
							int targetIndex = ((x + innerOffsetX) << (2*modifiedShift)) + ((y + innerOffsetY) << modifiedShift) + (z + innerOffsetZ);
							int blockType = value[sourceIndex] & 0xff;
							cubes[targetIndex] = (byte)blockType;
						}
					}
				}
				
				// compress section data and send it to storage
				byte[] compressedCubes = Cubes.createFromCubes(GeometryConstants.SECTION_CLUSTER_SIZE, cubes).compressToByteArray(GeometryConstants.SECTION_CLUSTER_SIZE);
				storage.saveSectionRelatedObject(sectionDataId, compressedCubes);
				
			}
		}
		
		/* (non-Javadoc)
		 * @see name.martingeisse.stackd.util.NbtParser#handleInt(java.lang.String, int)
		 */
		@Override
		protected void handleInt(String tagName, int value) {
			if (tagName.equals("xPos")) {
				if (value != currentChunkX) {
					throw new RuntimeException("xPos in chunk is incorrect");
				}
			} else if (tagName.equals("zPos")) {
				if (value != currentChunkZ) {
					throw new RuntimeException("zPos in chunk is incorrect");
				}
			}
		}
		
	}
	
}
