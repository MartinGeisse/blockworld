/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.glworker;

/**
 * Base interface for all work units executed by the OpenGL worker thread.
 */
public interface GlWorkUnit {

	/**
	 * Executes this work unit.
	 */
	public abstract void execute();
	
}
