/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.glworker;

/**
 * Base implementation for {@link SingleWorkUnitVisualTemplate}.
 * 
 * @param <T> the type of object represented by this template
 */
public abstract class AbstractSingleWorkUnitVisualTemplate<T> implements SingleWorkUnitVisualTemplate<T> {

	// override
	@Override
	public final void render(T subject) {
		GlWorkerLoop.getInstance().schedule(createWorkUnit(subject));
	}

	// override
	@Override
	public final GlWorkUnit createWorkUnit(final T subject) {
		return new GlWorkUnit() {
			@Override
			public void execute() {
				renderEmbedded(subject);
			}
		};
	}
	
}
