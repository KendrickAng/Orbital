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

    // TODO: Have render check if the skills are still persisting, and draw the necessary stuff.
    @Override
    public void render() {
        // draw character
        ShapeRenderer shape = super.getRenderer();
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.BLACK);
        shape.rect(getX(), getY(), getWidth(), getHeight());
        shape.end();

        // update persist states
        if(state.isPrimaryPressed()) {
            primary(); // no cooldown timer for primary skill
        }
        if(state.isSecondaryPressed()) {
            if(super.isSkillPersisting(state.getSecondaryTimeSince(), state.SECONDARY_PERSIST_TIME)) {
                secondary();
            } else {
                state.setSecondaryPressed(false);
            }
        }
        if(state.isTertiaryPressed()) {
            // TODO
            if(super.isSkillPersisting(state.getTertiaryTimeSince(), state.TERTIARY_PERSIST_TIME)) {
                tertiary();
            } else {
                // persist state is over
                state.setTertiaryPressed(false);
            }
        }
    }

    // Block
    @Override
    public void primary() {
        if(super.isSkillAvailable(state.getPrimaryTimeSince(), state.PRIMARY_COOLDOWN)) {
            state.updatePrimaryTimeSince();
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
        if(super.isSkillAvailable(state.getSecondaryTimeSince(), state.SECONDARY_COOLDOWN)) {
            state.updateSecondaryTimeSince(); // update last cast timing
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
        if(super.isSkillAvailable(state.getTertiaryTimeSince(), state.TERTIARY_COOLDOWN)) {
            state.updateTertiaryTimeSince(); // update last cast timing
            ShapeRenderer shape = super.getRenderer();
            while(super.isSkillPersisting(state.getTertiaryTimeSince(), state.TERTIARY_PERSIST_TIME)) {
                shape.begin(ShapeRenderer.ShapeType.Filled);
                shape.setColor(Color.GOLD);
                shape.rect(getX(), getY(), getWidth(), getHeight());
                shape.end();
            }
        }
    }

    @Override
    public void setPrimaryPressed(boolean flag) {
        this.state.setPrimaryPressed(flag);
    }

    @Override
    public void setSecondaryPressed(boolean flag) {
        this.state.setSecondaryPressed(flag);
    }

    @Override
    public void setTertiaryPressed(boolean flag) {
        this.state.setTertiaryPressed(flag);
    }
}
