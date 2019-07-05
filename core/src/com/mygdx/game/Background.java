package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.assets.Assets;

import static com.mygdx.game.MyGdxGame.GAME_HEIGHT;
import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.MyGdxGame.MAP_HEIGHT;
import static com.mygdx.game.assets.Assets.TextureName.BACKGROUND;
import static com.mygdx.game.assets.Assets.TextureName.FLOOR;

public class Background {
	private Texture background;
	private Texture floor;

	public Background(Assets assets) {
		floor = assets.getTexture(FLOOR);
		background = assets.getTexture(BACKGROUND);
		background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
	}

	public void render(SpriteBatch batch) {
		batch.draw(background, 0, 0, 0, 0, GAME_WIDTH, GAME_HEIGHT);
		batch.draw(floor, 0, 0, GAME_WIDTH, MAP_HEIGHT);
	}
}
