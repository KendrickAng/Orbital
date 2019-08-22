package com.untitled.screens;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.untitled.UntitledGame;
import com.untitled.assets.Assets;
import com.untitled.assets.MusicName;

/**
 * Setttings screen of Untitled.
 */
public abstract class SettingsScreen extends UntitledScreen {
	private Assets A;

	public SettingsScreen(UntitledGame game) {
		super(game);
		this.A = game.getAssets();
	}

	@Override
	public void renderDebug(ShapeRenderer renderer) {

	}

	@Override
	public void pauseScreen() {
		A.getMusic(MusicName.MAIN_MENU).pause();
	}

	@Override
	public void resumeScreen() {
		A.getMusic(MusicName.MAIN_MENU).play();
	}
}
