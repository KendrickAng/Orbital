package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.MyGdxGame.MAP_HEIGHT;

public class Background {
    private ShapeRenderer shape;

    public Background() {
        this.shape = new ShapeRenderer();
    }

    public void render() {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.BROWN);
        shape.rect(0, 0, GAME_WIDTH, MAP_HEIGHT);
        shape.end();
    }

    public void dispose() {
        shape.dispose();
    }
}
