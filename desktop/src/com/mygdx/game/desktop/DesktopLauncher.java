package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.UntitledGame;

import static com.mygdx.game.UntitledGame.CAMERA_HEIGHT;
import static com.mygdx.game.UntitledGame.CAMERA_WIDTH;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		// set game width and height
		config.width = CAMERA_WIDTH;
		config.height = CAMERA_HEIGHT;
		config.vSyncEnabled = true;
		config.foregroundFPS = 0;
		config.backgroundFPS = 0;
		new LwjglApplication(new UntitledGame(), config);
	}
}
