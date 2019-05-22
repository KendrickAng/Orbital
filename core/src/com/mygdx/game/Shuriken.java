package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import static com.mygdx.game.state.EntityStates.FLYING;

public class Shuriken extends Entity {
    public static final int FLYING_SPEED = 20;

    public Shuriken() {
        super();
    }

    @Override
    protected Animations<Entity> animations() {
        return new Animations<Entity>()
            .add(FLYING, new Texture(Gdx.files.internal("Entity/shuriken.png")), 1);
    }
}
