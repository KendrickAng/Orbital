package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import static com.mygdx.game.state.EntityStates.FLYING;
import static com.mygdx.game.texture.Textures.ENTITY_SHURIKEN;

public class Shuriken extends Entity {
    public static final int FLYING_SPEED = 20;

    public Shuriken(MyGdxGame game) {
        super(game);
    }

    @Override
    protected Animations<Entity> animations() {
        return new Animations<Entity>()
            .add(FLYING, getGame().getTextureManager().get(ENTITY_SHURIKEN), 1);
    }
}
