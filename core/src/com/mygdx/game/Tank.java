package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import static com.mygdx.game.MyGdxGame.*;

/**
 * Represents the Tank playable character.
 */
public class Tank extends Character {
    // load textures
    static {
        TANK_STANDING = new Sprite(new Texture(Gdx.files.internal("Tank/Standing.png")));
        TANK_PRIMARY = new Sprite(new Texture(Gdx.files.internal("Tank/Primary.png")));
        TANK_SECONDARY = new Sprite(new Texture(Gdx.files.internal("Tank/Secondary.png")));
    }

    private static float SHIELD_OFFSET; // affects primary block hitbox
    private static float SWORD_LENGTH; // affects secondary slash hitbox
    private static float SWORD_WIDTH;
    private static Sprite TANK_STANDING;
    private static Sprite TANK_PRIMARY;
    private static Sprite TANK_SECONDARY;

    private TankState state;
    private MyGdxGame game;
    private ShapeRenderer shape;

    /**
     * Initliases the tank at coordinates (0, MAP_HEIGHT).
     */
    public Tank() {
        super(TANK_STANDING.getWidth(), TANK_STANDING.getHeight());
        SHIELD_OFFSET = super.getHeight() / 10;
        SWORD_LENGTH = super.getHeight();
        SWORD_WIDTH = super.getWidth() / 2;
        state = new TankState();
        shape = new ShapeRenderer();
    }

    // TODO: don't use setters and getters, use protected boolean flags instead. ugh
    @Override
    public void render() {
        // draw character
        SpriteBatch batch = MyGdxGame.getSpriteBatch();
        TANK_STANDING.setPosition(getX(), getY());
        batch.begin();
        TANK_STANDING.draw(batch);
        batch.end();
//        ShapeRenderer shape = super.getRenderer();
//        shape.begin(ShapeRenderer.ShapeType.Filled);
//        shape.setColor(Color.BLACK);
//        shape.rect(getX(), getY(), getWidth(), getHeight());
//        shape.end();

        // update persist states
        if (state.isPrimaryPressed()) {
            primary(); // no cooldown timer for primary skill
            state.setPrimaryPressed(false);
        }

        if (state.isSecondaryPressed()) {
            if (super.isSkillAvailable(state.getSecondaryTimeSince(), state.SECONDARY_COOLDOWN) && !state.secondary_persist) {
                // case 1 : skill is available to cast.
                Gdx.app.log("Tank.java", "Secondary skill pressed");
                state.updateSecondaryTimeSince(); // update last cast timing to now
                state.secondary_persist = true;
                secondary();
            } else if(super.isSkillPersisting(state.getSecondaryTimeSince(), state.SECONDARY_PERSIST_TIME)) {
                // case 2: skill is on cooldown, but is persisting.
                secondary();
            } else {
                state.setSecondaryPressed(false);
                state.secondary_persist = false;
            }
        }

        if (state.isTertiaryPressed()) {
            // persist time may be longer than cooldown time, causing persist -> skill available -> persist...
            if (super.isSkillAvailable(state.getTertiaryTimeSince(), state.TERTIARY_COOLDOWN) && !state.tertiary_persist) {
                Gdx.app.log("Tank.java", "Tertiary skill pressed");
                state.updateTertiaryTimeSince();
                state.tertiary_persist = true;
                tertiary();
            } else if(super.isSkillPersisting(state.getTertiaryTimeSince(), state.TERTIARY_PERSIST_TIME)) {
                tertiary();
            } else {
                state.setTertiaryPressed(false);
                state.tertiary_persist = false;
            }
        }
    }

    @Override
    public void dispose() {
        shape.dispose();
    }

    // Block
    @Override
    public void primary() {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.GOLD);
        shape.rect(super.getX() - SHIELD_OFFSET, super.getY() - SHIELD_OFFSET,
                getWidth() + 2 * SHIELD_OFFSET, getHeight() + 2 * SHIELD_OFFSET);
        shape.end();
    }

    // Slash
    @Override
    public void secondary() {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.GOLD);
        shape.rect(getX() + getWidth() / 2, getY() + getHeight() / 2,
                SWORD_LENGTH, SWORD_WIDTH);
        shape.end();
    }

    // Fortress
    @Override
    public void tertiary() {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.GOLD);
        shape.rect(getX(), getY(), getWidth(), getHeight());
        shape.end();
    }

    @Override
    public void setPrimaryPressed(boolean flag) { this.state.setPrimaryPressed(flag); }
    @Override
    public void setSecondaryPressed(boolean flag) { this.state.setSecondaryPressed(flag); }
    @Override
    public void setTertiaryPressed(boolean flag) { this.state.setTertiaryPressed(flag); }
}
