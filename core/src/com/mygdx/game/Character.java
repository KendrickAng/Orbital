package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.awt.*;

import static com.mygdx.game.MyGdxGame.*;

public abstract class Character {
    private int x; // bottom left
    private int y; // bottom left
    private int width; // dimensions of rectangle
    private int height;
    private ShapeRenderer shape;

    public Character(int x, int y) {
        this.x = x;
        this.y = y; // account for map height offset
        this.width = PLAYER_WIDTH;
        this.height = PLAYER_HEIGHT;
        shape = new ShapeRenderer();
    }

    public Character() {
        this.x = 0;
        this.y = MAP_HEIGHT;
        shape = new ShapeRenderer();
    }

    public void render() {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.BLACK);
        shape.rect(x, y, width, height);
        shape.end();
    }


    public void dispose() {
        shape.dispose();
    }

    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public int getWidth() { return this.width; }
    public int getHeight() { return this.height; }
    public ShapeRenderer getRenderer() { return this.shape; }

    // Skills to be implemented
    public abstract void primary();
    public abstract void secondary();
    public abstract void tertiary();
    public abstract Character move(int x, int y);
}
