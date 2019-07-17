package com.mygdx.game.screens.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.assets.Assets;

import static com.mygdx.game.UntitledGame.CAMERA_HEIGHT;
import static com.mygdx.game.UntitledGame.CAMERA_WIDTH;
import static com.mygdx.game.assets.TextureName.BACKGROUND;

public class Background {
	private Texture background;

	public Background(Assets assets) {
		background = assets.getTexture(BACKGROUND);
		background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
	}

	public void render(SpriteBatch batch) {
		batch.draw(background, 0, 0, 0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
	}
}
