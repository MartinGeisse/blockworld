/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.server;

import org.apache.log4j.Logger;
import com.google.inject.Inject;

/**
 *
 */
public class ServerLauncher implements Runnable {

	private static Logger logger = Logger.getLogger(ServerLauncher.class);

	private final ServerLoop loop;

	/**
	 * Constructor.
	 * @param loop the server's main loop
	 */
	@Inject
	public ServerLauncher(final ServerLoop loop) {
		this.loop = loop;
	}

	// override
	@Override
	public void run() {
		try {
			loop.loop();
		} catch (InterruptedException e) {
			logger.error("interrupted", e);
		}
	}

}
