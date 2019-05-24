package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.ability.Abilities;

import static com.mygdx.game.state.EntityStates.*;
import static com.mygdx.game.texture.Textures.ASSASSIN_STANDING;
import static com.mygdx.game.texture.Textures.ASSASSIN_PRIMARY;
import static com.mygdx.game.texture.Textures.ASSASSIN_SECONDARY;
import static com.mygdx.game.texture.Textures.ASSASSIN_TERTIARY;

public class Assassin extends Character {
    // Skill cd in seconds.
    private static final float PRIMARY_COOLDOWN = 0;
    private static final float SECONDARY_COOLDOWN = 1;
    private static final float TERTIARY_COOLDOWN = 2;

    // Skill lasting time in seconds.
    private static final float PRIMARY_DURATION = 0.05f;
    private static final float SECONDARY_DURATION = 0.05f;
    private static final float TERTIARY_DURATION = 5;

    // influencing dodging
    private static final int STRAFE_SPEED = 5;
    private static final int JUMP_SPEED = 15;

    public Assassin(MyGdxGame game) {
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
        Texture assassin_standing = getGame().getTextureManager().get(ASSASSIN_STANDING);
        Texture assassin_primary = getGame().getTextureManager().get(ASSASSIN_PRIMARY);
        Texture assassin_secondary = getGame().getTextureManager().get(ASSASSIN_SECONDARY);
        Texture assassin_tertiary = getGame().getTextureManager().get(ASSASSIN_TERTIARY);

        return new Animations<Character>()
                .add(STANDING, assassin_standing, 1)
                .add(PRIMARY, assassin_primary, 2)
                .add(SECONDARY, assassin_secondary, 2)
                .add(TERTIARY, assassin_tertiary, 2);
    }

    // Dodge
    @Override
    public void isPrimary(ShapeRenderer shapeBatch) {
        Gdx.app.log("Assassin.java", "Primary");
        int x_vel = 0;
        int y_vel = JUMP_SPEED;
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x_vel = STRAFE_SPEED;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x_vel = -STRAFE_SPEED;
        }
        super.setSpeed(x_vel, y_vel);
    }

    // Stars
    @Override
    public void isSecondary(ShapeRenderer shapeBatch) {
        Gdx.app.log("Assassin.java", "Secondary");
        Entity shuriken = new Shuriken();
        int x = getX() + (int) (getWidth() / 2);
        int y = getY() + (int) (getHeight() / 2);
        int x_velocity = getDirection() == Direction.RIGHT ? Shuriken.FLYING_SPEED : -Shuriken.FLYING_SPEED;
        shuriken.setPosition(x, y);
        shuriken.setVelocity(x_velocity, 0);
        super.getGame().getEntityManager().add(shuriken);
    }

    // Cleanse
    @Override
    public void isTertiary(ShapeRenderer shapeBatch) {
        Gdx.app.log("Assassin.java", "Tertiary");
        shapeBatch.setColor(Color.GOLD);
        shapeBatch.rect(getX(), getY(), getWidth(), getHeight());
    }
}
