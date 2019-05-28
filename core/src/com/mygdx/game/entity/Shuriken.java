package com.mygdx.game.entity;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Animations;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.state.States;

import static com.mygdx.game.state.ShurikenStates.FLYING;
import static com.mygdx.game.texture.Textures.SHURIKEN_FLYING;

public class Shuriken extends Entity<Shuriken> {
	public static final int FLYING_SPEED = 20;

	public Shuriken(MyGdxGame game) {
		super(game);
	}

	@Override
	protected States<Shuriken> states() {
		return new States<Shuriken>()
				.add(FLYING);
	}

	@Override
	protected Animations<Shuriken> animations() {
		return new Animations<Shuriken>()
				.add(FLYING, getGame().getTextureManager().get(SHURIKEN_FLYING), 1);
	}

	@Override
	protected void updatePosition(Vector2 position) {

	}

	@Override
	protected void updateVelocity(Vector2 position, Vector2 velocity) {

	}
}
