package com.untitled.highscores;

/**
 * A Consumer.
 */
public interface NewUserCallback {
	/**
	 * @param token the token of the new (anonymous) user.
	 */
	void call(String token);
}
