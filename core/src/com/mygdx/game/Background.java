package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.texture.TextureManager;

import static com.mygdx.game.MyGdxGame.GAME_HEIGHT;
import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.MyGdxGame.MAP_HEIGHT;
import static com.mygdx.game.texture.Textures.ENVIRONMENT_BACKGROUND;
import static com.mygdx.game.texture.Textures.ENVIRONMENT_FLOOR;

public class Background {
    private Texture background;
    private Texture floor;
    public Background(TextureManager textureManager) {
        background = textureManager.get(ENVIRONMENT_BACKGROUND);
        // TODO: This should be in TextureManager (i.e. shouldn't be mutable)
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        floor = textureManager.get(ENVIRONMENT_FLOOR);
    }

    public void render(SpriteBatch batch) {
        batch.draw(background, 0, 0, 0, 0, GAME_WIDTH, GAME_HEIGHT);
        batch.draw(floor, 0, 0, GAME_WIDTH, MAP_HEIGHT);
    }
}
