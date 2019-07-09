package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.UntitledGame;

import static com.mygdx.game.UntitledGame.GAME_HEIGHT;
import static com.mygdx.game.UntitledGame.GAME_WIDTH;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		// set game width and height
		config.width = GAME_WIDTH;
		config.height = GAME_HEIGHT;
		new LwjglApplication(new UntitledGame(), config);
	}
}
