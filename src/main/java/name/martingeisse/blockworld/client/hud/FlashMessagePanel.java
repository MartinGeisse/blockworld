/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.client.hud;

import static org.lwjgl.opengl.GL14.glWindowPos2i;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.log4j.Logger;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import name.martingeisse.blockworld.client.assets.MinerResources;
import name.martingeisse.blockworld.client.glworker.GlWorkUnit;
import name.martingeisse.blockworld.client.glworker.GlWorkerLoop;
import name.martingeisse.blockworld.client.util.resource.Font;

/**
 * Displays one or more messages for a certain time. Each message will stay
 * for the same configurable time. Multiple messages will be stacked vertically
 * if necessary.
 */
public class FlashMessagePanel implements HudElement {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(FlashMessagePanel.class);
	
	/**
	 * the displayTime
	 */
	private long displayTime;

	/**
	 * the fadeTime
	 */
	private long fadeTime;

	/**
	 * the leftOffset
	 */
	private int leftOffset;

	/**
	 * the topOffset
	 */
	private int topOffset;

	/**
	 * the queue
	 */
	private final ConcurrentLinkedQueue<Entry> queue;

	/**
	 * the glWorkUnit
	 */
	private final GlWorkUnit glWorkUnit = new GlWorkUnit() {
		@Override
		public void execute() {
			Font font = MinerResources.getInstance().getFont();
			int height = Display.getHeight();
			final long now = System.currentTimeMillis();
			int i = 0;
			for (final Entry entry : queue) {
				final long age = (now - entry.publishTime);
				float brightness;
				if (age < displayTime) {
					brightness = 1.0f;
				} else {
					brightness = 1.0f - ((age - displayTime) / (float)fadeTime);
				}
				GL11.glPixelTransferf(GL11.GL_ALPHA_SCALE, brightness);
				glWindowPos2i(leftOffset, height - topOffset - i * (2 * font.getCharacterHeight() + 4));
				font.drawText(entry.message, 2, Font.ALIGN_LEFT, Font.ALIGN_TOP);
				i++;
			}
		}
	};

	/**
	 * Constructor.
	 */
	public FlashMessagePanel() {
		this.displayTime = 3000;
		this.fadeTime = 1000;
		this.leftOffset = 0;
		this.topOffset = 0;
		this.queue = new ConcurrentLinkedQueue<FlashMessagePanel.Entry>();
	}

	/**
	 * Getter method for the displayTime.
	 * @return the displayTime
	 */
	public final long getDisplayTime() {
		return displayTime;
	}

	/**
	 * Setter method for the displayTime.
	 * @param displayTime the displayTime to set
	 */
	public final void setDisplayTime(final long displayTime) {
		this.displayTime = displayTime;
	}

	/**
	 * Getter method for the fadeTime.
	 * @return the fadeTime
	 */
	public final long getFadeTime() {
		return fadeTime;
	}

	/**
	 * Setter method for the fadeTime.
	 * @param fadeTime the fadeTime to set
	 */
	public final void setFadeTime(final long fadeTime) {
		this.fadeTime = fadeTime;
	}

	/**
	 * Getter method for the leftOffset.
	 * @return the leftOffset
	 */
	public int getLeftOffset() {
		return leftOffset;
	}

	/**
	 * Setter method for the leftOffset.
	 * @param leftOffset the leftOffset to set
	 */
	public void setLeftOffset(final int leftOffset) {
		this.leftOffset = leftOffset;
	}

	/**
	 * Getter method for the topOffset.
	 * @return the topOffset
	 */
	public int getTopOffset() {
		return topOffset;
	}

	/**
	 * Setter method for the topOffset.
	 * @param topOffset the topOffset to set
	 */
	public void setTopOffset(final int topOffset) {
		this.topOffset = topOffset;
	}

	/**
	 * Adds a message to show to the user.
	 * @param message the message to add
	 */
	public final void addMessage(final String message) {
		logger.info("Flash message: " + message);
		queue.add(new Entry(System.currentTimeMillis(), message));
	}

	// override
	@Override
	public void draw() {
		final long now = System.currentTimeMillis();
		final long totalTimeToLive = (displayTime + fadeTime);
		while (!queue.isEmpty()) {
			final Entry next = queue.peek();
			final long age = (now - next.publishTime);
			if (age < totalTimeToLive) {
				break;
			}
			queue.remove();
		}
		GlWorkerLoop.getInstance().schedule(glWorkUnit);
	}

	/**
	 * Used to represent messages being displayed.
	 */
	final static class Entry {

		/**
		 * the publishTime
		 */
		final long publishTime;

		/**
		 * the message
		 */
		final String message;

		/**
		 * Constructor.
		 * @param publishTime the time at which this message was published
		 * @param message the message text
		 */
		Entry(final long publishTime, final String message) {
			this.publishTime = publishTime;
			this.message = message;
		}

	}

}
