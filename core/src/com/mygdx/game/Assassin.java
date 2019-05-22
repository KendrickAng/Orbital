package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.ability.Abilities;

import static com.mygdx.game.state.CharacterStates.*;

public class Assassin extends Character {
    // Skill cd in seconds.
    private static final float PRIMARY_COOLDOWN = 0;
    private static final float SECONDARY_COOLDOWN = 1;
    private static final float TERTIARY_COOLDOWN = 2;

    // Skill lasting time in seconds.
    private static final float PRIMARY_DURATION = 0.05f;
    private static final float SECONDARY_DURATION = 0.05f;
    private static final float TERTIARY_DURATION = 5;

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
                .add(PRIMARY, new Texture(Gdx.files.internal("Assassin/Primary.png")), 4)
                .add(SECONDARY, new Texture(Gdx.files.internal("Assassin/Secondary.png")), 2)
                .add(TERTIARY, new Texture(Gdx.files.internal("Assassin/Tertiary.png")), 3);
    }

    // Dodge
    @Override
    public void isPrimary(ShapeRenderer shapeBatch) {
        Gdx.app.log("Assassin.java", "Primary");

    }

    // Stars
    @Override
    public void isSecondary(ShapeRenderer shapeBatch) {
        Gdx.app.log("Assassin.java", "Primary");

    }

    // Cleanse
    @Override
    public void isTertiary(ShapeRenderer shapeBatch) {
        Gdx.app.log("Assassin.java", "Primary");

    }
}
