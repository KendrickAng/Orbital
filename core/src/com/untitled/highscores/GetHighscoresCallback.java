package com.untitled.highscores;

import com.badlogic.gdx.utils.Array;

/**
 * A Consumer.
 */
public interface GetHighscoresCallback {
	/**
	 * @param highscores an array of highscores
	 */
	void call(Array<Highscore> highscores);
}
