/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.common.cubes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import name.martingeisse.blockworld.common.geometry.GeometryConstants;
import name.martingeisse.blockworld.geometry.ReadableVector3i;

/**
 * Base class for cube matrix implementations. This class also provides
 * (de-)serialization to/from byte arrays.
 */
public abstract class Cubes {

	/**
	 * This cubes object is guaranteed to be immutable, so it can be used for uninitialized
	 * new sections. Any modification will result in a new {@link Cubes} being built.
	 */
	public static final Cubes UNINITIALIZED = new UniformCubes((byte)0);
	
	/**
	 * Constructor. Made package-private since this class hierarchy is
	 * not extensible.
	 */
	Cubes() {
	}

	/**
	 * Serializes and compresses this cubes object, writing compressed data to the
	 * specified stream (including the compression scheme code).
	 * 
	 * @param stream the stream to write compressed data to
	 * @throws IOException on I/O errors
	 */
	public final void compressToStream(final OutputStream stream) throws IOException {
		if (stream == null) {
			throw new IllegalArgumentException("stream argument cannot be null");
		}
		compressToStreamInternal(stream);
	}

	/**
	 * Implementation for {@link #compressToStream(OutputStream)}.
	 * @throws IOException on I/O errors
	 */
	protected abstract void compressToStreamInternal(OutputStream stream) throws IOException;

	/**
	 * Serializes and compresses this cubes object, writing compressed data to a
	 * new byte array (including the compression scheme code).
	 * 
	 * @return the compressed data
	 */
	public final byte[] compressToByteArray() {
		try {
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			compressToStream(byteArrayOutputStream);
			return byteArrayOutputStream.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Creates a cubes instance from compressed data.
	 * 
	 * @param compressedData the compressed data
	 * @return the cubes object
	 */
	public static Cubes createFromCompressedData(final byte[] compressedData) {
		if (compressedData == null) {
			throw new IllegalArgumentException("compressedData argument cannot be null");
		}
		final int compressionSchemeCode = (compressedData.length > 0 ? compressedData[0] : 0);
		switch (compressionSchemeCode) {

		case 0:
			return UniformCubes.decompress(compressedData);

		case 1:
			return RawCubes.decompress(compressedData);

		default:
			throw new RuntimeException("unknown compression scheme code: " + compressionSchemeCode);

		}
	}

	/**
	 * Creates a cubes instance from cube data. The cubes must
	 * be XYZ-ordered, i.e. an 1-increment in Z results in a
	 * 1-increment in the array index.
	 * 
	 * TODO use {@link GeometryConstants#SECTION_CLUSTER_SIZE} by default -- no need to pass
	 * it around everywhere as if this was a framework
	 * 
	 * @param cubes the raw cubes
	 * @return the cubes object
	 */
	public static Cubes createFromCubes(final byte[] cubes) {
		if (cubes == null) {
			throw new IllegalArgumentException("cubes argument cannot be null");
		}
		if (cubes.length != GeometryConstants.SECTION_CLUSTER_SIZE.getCellCount()) {
			throw new IllegalArgumentException("the specified cube data has wrong size " + cubes.length + " (should be " + GeometryConstants.SECTION_CLUSTER_SIZE.getCellCount() + ")");
		}
		final UniformCubes uniformCubes = UniformCubes.tryBuild(cubes);
		if (uniformCubes != null) {
			return uniformCubes;
		}
		return RawCubes.build(cubes);
	}

	/**
	 * Returns a list of cube type indices present in this cubes object.
	 * @return the cube type indices
	 */
	public abstract byte[] getCubeTypeIndicesUsed();

	/**
	 * Returns the cube value for the specified relative position.
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
	 * @return the cube value
	 */
	public abstract byte getCubeRelative(final int x, final int y, final int z);

	/**
	 * Returns the cube value for the specified relative position.
	 * 
	 * @param position the position
	 * @return the cube value
	 */
	public final byte getCubeRelative(final ReadableVector3i position) {
		return getCubeRelative(position.getX(), position.getY(), position.getZ());
	}
	
	/**
	 * Sets the cube value for the specified relative position. This method may fail to update
	 * this object in-place and return a new {@link Cubes} object instead, which should then
	 * be used instead of this object. In either case, this method returns the {@link Cubes}
	 * instance to use after the modification (which is this object if it was able to handle the
	 * modification in-place).
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
	 * @param value the cube value to set
	 * @return the cubes object to use after the modification
	 */
	public abstract Cubes setCubeRelative(final int x, final int y, final int z, final byte value);
	
	/**
	 * Sets the cube value for the specified relative position. This method may fail to update
	 * this object in-place and return a new {@link Cubes} object instead, which should then
	 * be used instead of this object. In either case, this method returns the {@link Cubes}
	 * instance to use after the modification (which is this object if it was able to handle the
	 * modification in-place).
	 * 
	 * @param position the position
	 * @param value the cube value to set
	 * @return the cubes object to use after the modification
	 */
	public final Cubes setCubeRelative(final ReadableVector3i position, final byte value) {
		return setCubeRelative(position.getX(), position.getY(), position.getZ(), value);
	}

	/**
	 * Creates a new {@link RawCubes} from the data in this object, or returns
	 * this if it already is an instance of that class.
	 * 
	 * @return the {@link RawCubes} object.
	 */
	public abstract RawCubes convertToRawCubes();
	
	// override
	@Override
	public abstract Cubes clone();
	
}
