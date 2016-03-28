/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.blockworld.common.network.c2s_message;

/**
 * Resumes playing a character. This message is sent when starting to play.
 */
public final class ResumeCharacterMessage implements ClientToServerMessage {

	private final String playCharacterToken;

	/**
	 * Constructor.
	 * @param playCharacterToken the play-character token
	 */
	public ResumeCharacterMessage(final String playCharacterToken) {
		this.playCharacterToken = playCharacterToken;
	}

	/**
	 * Getter method for the playCharacterToken.
	 * @return the playCharacterToken
	 */
	public String getPlayCharacterToken() {
		return playCharacterToken;
	}

}
