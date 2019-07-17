package com.mygdx.game.screens.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.assets.Assets;

import static com.mygdx.game.UntitledGame.CAMERA_WIDTH;
import static com.mygdx.game.UntitledGame.FLOOR_HEIGHT;
import static com.mygdx.game.assets.TextureName.FLOOR;

public class Floor {
	private Texture floor;

	public Floor(Assets assets) {
		floor = assets.getTexture(FLOOR);
	}

	public void render(SpriteBatch batch) {
		batch.draw(floor, 0, 0, CAMERA_WIDTH, FLOOR_HEIGHT);
	}
}
