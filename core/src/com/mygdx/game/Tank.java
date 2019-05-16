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

    private TankState state;
    /**
     * Initliases the tank at coordinates (0, MAP_HEIGHT).
     */
    public Tank() {
        super();
        state = new TankState();
    }

    // Block
    @Override
    public void primary() {
        if(super.isSkillAvailable(state.getPrimary(), state.PRIMARY_COOLDOWN)) {
            ShapeRenderer shape = super.getRenderer();
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(Color.GOLD);
            shape.rect(super.getX() - SHIELD_OFFSET, super.getY() - SHIELD_OFFSET,
                    super.getWidth() + 2 * SHIELD_OFFSET, super.getHeight() + 2 * SHIELD_OFFSET);
            shape.end();
        }
    }

    // Slash
    @Override
    public void secondary() {
        // execute skill only if cooldown is up
        if(super.isSkillAvailable(state.getSecondary(), state.SECONDARY_COOLDOWN)) {
            state.updateSecondary();
            ShapeRenderer shape = super.getRenderer();
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(Color.GOLD);
            shape.rect(getX() + getWidth() / 2, getY() + getHeight() / 2,
                    SWORD_LENGTH, SWORD_WIDTH);
            shape.end();
        }
    }

    // Fortress
    @Override
    public void tertiary() {

    }
}
