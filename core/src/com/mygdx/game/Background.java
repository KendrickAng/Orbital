package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.MyGdxGame.MAP_HEIGHT;

public class Background {
    public void renderShape(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.BROWN);
        shapeRenderer.rect(0, 0, GAME_WIDTH, MAP_HEIGHT);
    }
}
