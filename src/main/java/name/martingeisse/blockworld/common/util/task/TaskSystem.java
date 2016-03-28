/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.common.util.task;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * This class manages execution of tasks.
 */
public final class TaskSystem {

	/**
	 * Prevent instantiation.
	 */
	private TaskSystem() {
	}
	
	/**
	 * the executorService
	 */
	static final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(4);
	
	/**
	 * Initializes the task system. The thread calling this method is not, and cannot become,
	 * a worker thread since all workers are managed internally by the task system.
	 * 
	 * TODO consider refactoring such that the task system gets injected and that the task
	 * system is used to schedule tasks, i.e. tasks don't allow to schedule themselves
	 */
	public static void initialize() {
	}
	
	/**
	 * Getter method for the executorService.
	 * @return the executorService
	 */
	public static ScheduledExecutorService getExecutorService() {
		return executorService;
	}
	
}
