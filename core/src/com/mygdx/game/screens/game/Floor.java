package com.mygdx.game.screens.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.assets.Assets;

import static com.mygdx.game.UntitledGame.CAMERA_WIDTH;
import static com.mygdx.game.assets.TextureName.GAME_FLOOR;
import static com.mygdx.game.screens.GameScreen.GAME_FLOOR_HEIGHT;

public class Floor {
	private Texture floor;

	public Floor(Assets assets) {
		floor = assets.getTexture(GAME_FLOOR);
	}

	public void render(SpriteBatch batch) {
		batch.draw(floor, 0, 0, CAMERA_WIDTH, GAME_FLOOR_HEIGHT);
	}
}
