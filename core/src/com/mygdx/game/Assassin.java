package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.ability.Abilities;

import static com.mygdx.game.state.EntityStates.*;

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

    public Assassin() {
        super();
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
        return new Animations<Character>()
                .add(STANDING, new Texture(Gdx.files.internal("Assassin/Standing.png")), 1)
                .add(PRIMARY, new Texture(Gdx.files.internal("Assassin/Primary.png")), 2)
                .add(SECONDARY, new Texture(Gdx.files.internal("Assassin/Secondary.png")), 2)
                .add(TERTIARY, new Texture(Gdx.files.internal("Assassin/Tertiary.png")), 2);
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
        GameScreen.entityManager.add(shuriken);
    }

    // Cleanse
    // TODO: Can't dodge while in tertiary, when should be able to.
    @Override
    public void isTertiary(ShapeRenderer shapeBatch) {
        Gdx.app.log("Assassin.java", "Tertiary");
        shapeBatch.setColor(Color.GOLD);
        shapeBatch.rect(getX(), getY(), getWidth(), getHeight());
    }
}
