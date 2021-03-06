/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.common.util.task;

import java.util.concurrent.TimeUnit;

/**
 * Base interface for task-like objects that can be scheduled for execution.
 */
public interface TaskLike {

	/**
	 * Schedules this task-like object to run ASAP.
	 */
	public void schedule();
	
	/**
	 * Schedules this task-like object to run in N milliseconds.
	 * 
	 * @param delay the delay to wait before execution
	 * @param timeUnit the unit used for the delay
	 */
	public void scheduleRelative(long delay, TimeUnit timeUnit);
	
}
