package com.untitled.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.untitled.UntitledGame;
import com.untitled.assets.Assets;
import com.untitled.assets.TextureName;
import com.untitled.screens.GameScreen;

public class Floor {
	private Texture floor;

	public Floor(Assets assets) {
		floor = assets.getTexture(TextureName.GAME_FLOOR);
	}

	public void render(SpriteBatch batch) {
		batch.draw(floor, 0, 0, UntitledGame.CAMERA_WIDTH, GameScreen.GAME_FLOOR_HEIGHT);
	}
}
