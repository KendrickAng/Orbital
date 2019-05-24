package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.ability.Abilities;

import static com.mygdx.game.state.EntityStates.PRIMARY;
import static com.mygdx.game.state.EntityStates.SECONDARY;
import static com.mygdx.game.state.EntityStates.STANDING;
import static com.mygdx.game.state.EntityStates.TERTIARY;
import static com.mygdx.game.Direction.RIGHT;
import static com.mygdx.game.Direction.LEFT;
import static com.mygdx.game.texture.Textures.TANK_STANDING;
import static com.mygdx.game.texture.Textures.TANK_PRIMARY;
import static com.mygdx.game.texture.Textures.TANK_SECONDARY;
import static com.mygdx.game.texture.Textures.TANK_TERTIARY;

/**
 * Represents the Tank playable character.
 */
public class Tank extends Character {
    // Skill cd in seconds.
    private static final float PRIMARY_COOLDOWN = 0;
    private static final float SECONDARY_COOLDOWN = 1;
    private static final float TERTIARY_COOLDOWN = 2;

    // Skill lasting time in seconds.
    private static final float PRIMARY_DURATION = 0.05f;
    private static final float SECONDARY_DURATION = 0.05f;
    private static final float TERTIARY_DURATION = 5;

    public Tank(MyGdxGame game) {
        super(game);
    }

    @Override
    protected Abilities<Character> abilities() {
        return new Abilities<Character>()
                .add(PRIMARY, PRIMARY_COOLDOWN, PRIMARY_DURATION)
                .add(SECONDARY, SECONDARY_COOLDOWN, SECONDARY_DURATION)
                .add(TERTIARY, TERTIARY_COOLDOWN, TERTIARY_DURATION);
    }

    @Override
    protected Animations<Character> animations() {
        /*
         * Load Textures
         */
        Texture tank_standing = getGame().getTextureManager().get(TANK_STANDING);
        Texture tank_primary = getGame().getTextureManager().get(TANK_PRIMARY);
        Texture tank_secondary = getGame().getTextureManager().get(TANK_SECONDARY);
        Texture tank_tertiary = getGame().getTextureManager().get(TANK_TERTIARY);

        return new Animations<Character>()
                .add(STANDING, tank_standing, 1)
                .add(PRIMARY, tank_primary, 2)
                .add(SECONDARY, tank_secondary, 2)
                .add(TERTIARY, tank_tertiary, 2);
    }

    /* Block */
    @Override
    public void isPrimary(ShapeRenderer shapeBatch) {
        Gdx.app.log("Tank.java", "Primary");
        // affects primary block hitbox
        float SHIELD_OFFSET = super.getHeight() / 10;
        shapeBatch.setColor(Color.GOLD);
        shapeBatch.rect(super.getX() - SHIELD_OFFSET, super.getY() - SHIELD_OFFSET,
                getWidth() + 2 * SHIELD_OFFSET, getHeight() + 2 * SHIELD_OFFSET);
    }

    /* Slash */
    @Override
    public void isSecondary(ShapeRenderer shapeBatch) {
        Gdx.app.log("Tank.java", "Secondary");
        // affects secondary slash hitbox
        float SWORD_LENGTH = super.getHeight();
        float SWORD_WIDTH = super.getWidth() / 2;
        shapeBatch.setColor(Color.GOLD);
        shapeBatch.rect(getX() + getWidth() / 2, getY() + getHeight() / 2,
                SWORD_LENGTH, SWORD_WIDTH);
    }

    /* Fortress */
    @Override
    public void isTertiary(ShapeRenderer shapeBatch) {
        Gdx.app.log("Tank.java", "Tertiary");
        shapeBatch.setColor(Color.GOLD);
        shapeBatch.rect(getX(), getY(), getWidth(), getHeight());
    }
}
