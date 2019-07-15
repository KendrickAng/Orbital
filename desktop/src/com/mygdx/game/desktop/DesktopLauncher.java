package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.UntitledGame;

import static com.mygdx.game.UntitledGame.WINDOW_HEIGHT;
import static com.mygdx.game.UntitledGame.WINDOW_WIDTH;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		// set game width and height
		config.width = WINDOW_WIDTH;
		config.height = WINDOW_HEIGHT;
		new LwjglApplication(new UntitledGame(), config);
	}
}
