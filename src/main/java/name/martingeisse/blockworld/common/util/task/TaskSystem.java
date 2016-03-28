/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.common.util.task;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

/**
 * This class manages execution of tasks.
 */
public final class TaskSystem {

	private static Logger logger = Logger.getLogger(TaskSystem.class);

	/**
	 * Prevent instantiation.
	 */
	private TaskSystem() {
	}
	
	/**
	 * the executorService
	 */
	private static final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(4);
	
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
	 * Shuts down the task system.
	 */
	public static void shutdown() {
		executorService.shutdown();
		try {
			executorService.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Schedules the specified runnable.
	 * 
	 * @param r the runnable to schedule
	 */
	public static void schedule(Runnable r) {
		executorService.execute(wrap(r));
	}
	
	/**
	 * Schedules the specified runnable to run later.
	 * 
	 * @param r the runnable to schedule
	 * @param delay the amount of time to run in the future
	 * @param timeUnit the unit for the delay
	 */
	public static void schedule(Runnable r, long delay, TimeUnit timeUnit) {
		executorService.schedule(r, delay, timeUnit);
	}
	
	private static Runnable wrap(Runnable r) {
		return () -> {
			try {
				r.run();
			} catch (Throwable t) {
				logger.error("uncaught exception in task that ran in the task system", t);
			}
		};
	}
	
}
