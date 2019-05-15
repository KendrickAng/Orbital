package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Rectangle {
    private int x; // bottom left
    private int y; // bottom left
    private ShapeRenderer shape;

    public Rectangle(int x, int y) {
        this.x = x;
        this.y = y;
        shape = new ShapeRenderer();
    }

    public Rectangle() {
        shape = new ShapeRenderer();
    }

    public void render() {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.BLACK);
        shape.rect(x, y, 50, 100);
        shape.end();
    }

    public Rectangle move(int x, int y) {
        return new Rectangle(x, y);
    }

    public int getX() { return this.x; }
    public int getY() { return this.y; }
}
