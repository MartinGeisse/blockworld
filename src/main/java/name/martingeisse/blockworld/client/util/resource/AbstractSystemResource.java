/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.util.resource;

/**
 * Base implementation for {@link SystemResource} that prevents
 * double disposal.
 */
public abstract class AbstractSystemResource implements SystemResource {

	/**
	 * the disposed
	 */
	private boolean disposed;
	
	/**
	 * Constructor.
	 */
	public AbstractSystemResource() {
		this.disposed = false;
	}
	
	// override
	@Override
	public final void dispose() {
		if (disposed) {
			throw new IllegalStateException("this resource has already been disposed of");
		}
		internalDispose();
		disposed = true;
	}
	
	/**
	 * Actually disposes of this resource. Only called if not yet disposed.
	 */
	protected abstract void internalDispose();
	
}
