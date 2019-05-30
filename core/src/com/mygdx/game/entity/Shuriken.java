package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.animation.Animations;
import com.mygdx.game.animation.AnimationsGroup;
import com.mygdx.game.shape.Rectangle;

import java.util.HashSet;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.entity.Shuriken.Parts.BODY;
import static com.mygdx.game.entity.Shuriken.States.FLYING;

public class Shuriken extends Entity<Shuriken.States, Shuriken.Parts> {
	private static final float SHURIKEN_DAMAGE = 10;

	public enum States {
		FLYING
	}

	public enum Parts {
		BODY
	}

	public static final int FLYING_SPEED = 20;

	public Shuriken(GameScreen game) {
		super(game);
	}

	@Override
	protected void states(HashSet<States> states) {
		states.add(FLYING);
	}

	@Override
	protected Animations<States, Parts> animations() {
		AnimationsGroup<Parts> flying = new AnimationsGroup<Parts>("Assassin/Shuriken")
				.add(BODY, "Body")
				.load();

		return new Animations<States, Parts>(getStates())
				.add(FLYING, flying, 1)
				.done();
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

	@Override
	protected Rectangle hitbox() {
		return getAnimations().getHitbox(BODY);
	}
}
