/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.common.cubes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import name.martingeisse.blockworld.common.geometry.ClusterSize;
import name.martingeisse.blockworld.common.util.CompressionUtil;

/**
 * This type of cubes object stores a cube matrix directly. It is the
 * fallback data type if no other type works well.
 */
public class RawCubes extends Cubes {

	/**
	 * The DEFLATE dictionary used for compressing interactive section data.
	 */
	public static final byte[] COMPRESSION_DICTIONARY;
	static {
		ByteArrayOutputStream s = new ByteArrayOutputStream();
		// TODO reverse order (0xff first) might shorten the pointers and improve compression!
		for (int i=0; i<30; i++) {
			s.write(0x00);
		}
		for (int i=0; i<10; i++) {
			s.write(0x09);
		}
		for (int i=0; i<1000; i++) {
			s.write(0xff);
		}
		COMPRESSION_DICTIONARY = s.toByteArray();
	}

	/**
	 * the cubes
	 */
	private final byte[] cubes;

	/**
	 * Constructor.
	 */
	private RawCubes(final byte[] cubes) {
		this.cubes = cubes;
	}
	
	/**
	 * Getter method for the cubes.
	 * @return the cubes
	 */
	public byte[] getCubes() {
		return cubes;
	}
	
	// override
	@Override
	protected void compressToStreamInternal(final ClusterSize clusterSize, final OutputStream stream) throws IOException {
		
		// TODO try uniform
		// write cubes
		stream.write(1);
		stream.write(CompressionUtil.deflate(cubes, COMPRESSION_DICTIONARY));
		 
	}

	/**
	 * Decompresses and deserializes an object of this type from the specified array,
	 * skipping the first byte since it is assumed to contain the compression scheme.
	 * 
	 * @param clusterSize the cluster size
	 * @param compressedData the compressed data
	 * @return the cubes object, or null if not successful
	 */
	public static RawCubes decompress(final ClusterSize clusterSize, final byte[] compressedData) {
		final byte[] deflatedCubes = ArrayUtils.subarray(compressedData, 1, compressedData.length);
		final byte[] cubes = CompressionUtil.inflate(deflatedCubes, COMPRESSION_DICTIONARY);
		return new RawCubes(cubes);
	}

	/**
	 * Builds an instance of this type from the specified cube data.
	 * 
	 * @param cubes the cube data
	 * @return the cubes object
	 */
	public static RawCubes build(final byte[] cubes) {
		return new RawCubes(cubes);
	}

	/**
	 * Builds an instance of this type, filled uniformly with a single cube type.
	 * @param clusterSize the cluster size
	 * @param cubeType the cube type to fill the returned object with
	 * @return the cubes object
	 */
	public static RawCubes buildUniform(final ClusterSize clusterSize, final byte cubeType) {
		final byte[] cubes = new byte[clusterSize.getCellCount()];
		Arrays.fill(cubes, cubeType);
		return new RawCubes(cubes);
	}

	// override
	@Override
	public byte[] getCubeTypeIndicesUsed() {

		// build an array of flags that tell which cube types are used, as well
		// as the number of different cube types
		final boolean[] usedFlags = new boolean[256];
		int usedCount = 0;
		for (int i = 0; i < cubes.length; i++) {
			final int cubeTypeIndex = (cubes[i] & 0xff);
			if (!usedFlags[cubeTypeIndex]) {
				usedFlags[cubeTypeIndex] = true;
				usedCount++;
			}
		}

		// build the result array
		final byte[] result = new byte[usedCount];
		usedCount = 0;
		for (int i = 0; i < 256; i++) {
			if (usedFlags[i]) {
				result[usedCount] = (byte)i;
				usedCount++;
			}
		}
		return result;

	}

	// override
	@Override
	public byte getCubeRelative(final ClusterSize clusterSize, final int x, final int y, final int z) {
		return cubes[getRelativeCubeIndex(clusterSize, x, y, z)];
	}

	// override
	@Override
	public Cubes setCubeRelative(final ClusterSize clusterSize, final int x, final int y, final int z, final byte value) {
		cubes[getRelativeCubeIndex(clusterSize, x, y, z)] = value;
		return this;
	}

	/**
	 * 
	 */
	final int getRelativeCubeIndex(final ClusterSize clusterSize, final int x, final int y, final int z) {
		final int size = clusterSize.getSize();
		if (x < 0 || y < 0 || z < 0 || x >= size || y >= size || z >= size) {
			throw new IndexOutOfBoundsException();
		}
		return (x * size + y) * size + z;
	}

	// override
	@Override
	public RawCubes convertToRawCubes(ClusterSize clusterSize) {
		return this;
	}

	// override
	@Override
	public RawCubes clone() {
		return new RawCubes(Arrays.copyOf(cubes, cubes.length));
	}

}
