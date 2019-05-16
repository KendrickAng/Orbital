package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import static com.mygdx.game.MyGdxGame.*;

/**
 * Represents the Tank playable character.
 */
public class Tank extends Character {
    private static final int SHIELD_OFFSET = PLAYER_HEIGHT / 10;
    private static final int SWORD_LENGTH = PLAYER_HEIGHT;
    private static final int SWORD_WIDTH = PLAYER_WIDTH / 2;

    /**
     * Initliases the tank at coordinates (0, MAP_HEIGHT).
     */
    public Tank() {
        super();
    }

    /**
     * Initialises the tank at specified coordinates. (0, 0) is bottom-left corner.
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Tank(int x, int y) {
        super(x, y);
    }

    // Block
    @Override
    public void primary() {
        ShapeRenderer shape = super.getRenderer();
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.GOLD);
        shape.rect(super.getX() - SHIELD_OFFSET, super.getY() - SHIELD_OFFSET,
                super.getWidth() + 2 * SHIELD_OFFSET, super.getHeight() + 2 * SHIELD_OFFSET);
        shape.end();
    }

    @Override
    public void secondary() {
        ShapeRenderer shape = super.getRenderer();
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.GOLD);
        shape.rect(getX() + getWidth() / 2, getY() + getHeight() / 2,
                SWORD_LENGTH, SWORD_WIDTH);
        shape.end();
    }

    @Override
    public void tertiary() {

    }

    @Override
    public Character move(int x, int y) {
        return new Tank(x, y);
    }


}
