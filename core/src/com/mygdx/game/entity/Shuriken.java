package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Animations;
import com.mygdx.game.GameScreen;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.entity.Shuriken.MovingState.FLYING;
import static com.mygdx.game.texture.Textures.SHURIKEN_FLYING;

public class Shuriken extends Entity<Shuriken.MovingState> {
	private static final float SHURIKEN_DAMAGE = 10;

	public enum MovingState {
		FLYING
	}

	public static final int FLYING_SPEED = 20;

	public Shuriken(GameScreen game) {
		super(game);
	}

	@Override
	protected MovingState basicState() {
		return FLYING;
	}

	@Override
	protected Animations<MovingState> basicAnimations() {
		return new Animations<MovingState>()
				.add(FLYING, getGame().getTextureManager().get(SHURIKEN_FLYING), 1);
	}

	@Override
	protected void update() {
		Boss1 boss = getGame().getBoss();
		if (getHitbox().hitTest(boss.getHitbox())) {
			Gdx.app.log("Shuriken.java", "Boss was hit!");
			boss.damage(SHURIKEN_DAMAGE);
			dispose();
		}
	}

	@Override
	protected void updatePosition(Vector2 position) {
		if (position.x < 0) {
			dispose();
		}

		if (position.x > GAME_WIDTH - getWidth()) {
			dispose();
		}
	}

	@Override
	protected void updateVelocity(Vector2 position, Vector2 velocity) {

	}
}
