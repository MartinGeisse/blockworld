/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.common.util.task;

import java.util.concurrent.TimeUnit;

/**
 * Keeps a simple array of tasks or task-likes and allows to schedule them
 * all at once.
 */
public class TaskArray implements TaskLike {

	/**
	 * the tasks
	 */
	private TaskLike[] tasks;

	/**
	 * Constructor.
	 */
	public TaskArray() {
	}
	
	/**
	 * Constructor.
	 * @param tasks the tasks
	 */
	public TaskArray(final TaskLike[] tasks) {
		this.tasks = tasks;
	}

	/**
	 * Getter method for the tasks.
	 * @return the tasks
	 */
	public TaskLike[] getTasks() {
		return tasks;
	}
	
	/**
	 * Setter method for the tasks.
	 * @param tasks the tasks to set
	 */
	public void setTasks(TaskLike[] tasks) {
		this.tasks = tasks;
	}

	// override
	@Override
	public void schedule() {
		for (final TaskLike task : tasks) {
			task.schedule();
		}
	}

	// override
	@Override
	public void scheduleRelative(final long milliseconds) {
		for (final TaskLike task : tasks) {
			task.scheduleRelative(milliseconds);
		}
	}

	// override
	@Override
	public void scheduleRelative(final long delay, final TimeUnit timeUnit) {
		for (final TaskLike task : tasks) {
			task.scheduleRelative(delay, timeUnit);
		}
	}

}
