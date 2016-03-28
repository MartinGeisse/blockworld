/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.common.util.task;

import java.util.concurrent.TimeUnit;

/**
 * Base class for tasks that can be scheduled.
 */
public abstract class Task implements TaskLike, Runnable {

	/**
	 * Schedules this task to run ASAP.
	 */
	@Override
	public final void schedule() {
		TaskSystem.schedule(this);
	}
	
	/**
	 * Schedules this task to run in N milliseconds.
	 * 
	 * @param delay the delay to wait before execution
	 * @param timeUnit the unit used for the delay
	 */
	@Override
	public final void scheduleRelative(long delay, TimeUnit timeUnit) {
		TaskSystem.schedule(this, delay, timeUnit);
	}
	
}
