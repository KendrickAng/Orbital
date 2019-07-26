package com.untitled.highscores;

import com.badlogic.gdx.utils.Array;

public interface GetHighscoresCallback {
	void call(Array<Highscore> highscores);
}
