/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.common.util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class is similar to a {@link Timer} and {@link TimerTask}, but does
 * not have its own thread. Instead, it relies on being called by a thread
 * regularly and invokes its {@link Runnable} from that thread. The interval
 * is therefore somewhat uncertain since the callback can only be invoked
 * when that thread is calling this invoker.
 * 
 * The interval is specified in milliseconds since a more precise unit
 * would not be useful due to the uncertainty.
 * 
 * If the thread does not call this invoker often enough, whole intervals
 * may be missed. In this case, the runnable is still called only once to
 * catch up multiple intervals, so this class is not useful for cases where
 * missed calls cause major problems (fixed-frequency tasks), only for
 * idempotent, "update-like" tasks. Also, the timer restarts at the time
 * the handling method runs, and not at the time it should have run.
 */
public final class IntervalInvoker {

	private final long intervalMilliseconds;
	private long lastFiredTime;
	private Runnable callback;
	
	/**
	 * Constructor.
	 * @param intervalMilliseconds the length of the interval in milliseconds
	 * @param callback the callback to invoke
	 */
	public IntervalInvoker(long intervalMilliseconds, Runnable callback) {
		this.intervalMilliseconds = intervalMilliseconds;
		this.lastFiredTime = System.currentTimeMillis();
		this.callback = callback;
	}

	/**
	 * This method must be called regularly.
	 */
	public void step() {
		long currentTime = System.currentTimeMillis();
		if (currentTime > lastFiredTime + intervalMilliseconds) {
			callback.run();
			lastFiredTime = currentTime;
		}
	}
	
}
