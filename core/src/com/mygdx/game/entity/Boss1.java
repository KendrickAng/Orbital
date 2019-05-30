package com.mygdx.game.entity;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.ability.Abilities;
import com.mygdx.game.animation.Animations;
import com.mygdx.game.animation.AnimationsGroup;
import com.mygdx.game.entity.debuff.DebuffType;
import com.mygdx.game.entity.debuff.Debuffs;
import com.mygdx.game.shape.Rectangle;

import java.util.HashSet;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.MyGdxGame.MAP_HEIGHT;
import static com.mygdx.game.entity.Boss1.Parts.BODY;
import static com.mygdx.game.entity.Boss1.States.STANDING;

public class Boss1 extends LivingEntity<Boss1.States, Boss1.Parts> {
	private static final float HEALTH = 1000;

	public enum States {
		STANDING, WALKING,
		PRIMARY, SECONDARY
	}

	public enum Parts {
		BODY
	}

	public Boss1(GameScreen game) {
		super(game);
		setPosition(GAME_WIDTH - getWidth(), MAP_HEIGHT);
	}

	@Override
	protected void states(HashSet<States> states) {
		states.add(STANDING);
	}

	@Override
	protected float health() {
		return HEALTH;
	}

	@Override
	protected Abilities<States> abilities() {
		return new Abilities<>(getStates());
	}


	/* Animations */
	@Override
	protected Animations<States, Parts> animations() {
		AnimationsGroup<Parts> standing = new AnimationsGroup<Parts>("Boss1/Standing")
				.add(BODY, "Body")
				.load();

		return new Animations<States, Parts>(getStates())
				.add(STANDING, standing, 1)
				.done();
	}

	@Override
	protected Debuffs<DebuffType> debuffs() {
		return new Debuffs<>();
	}

	/* Update */
	@Override
	protected void updateDirection(Direction inputDirection) {

	}

	@Override
	protected void updatePosition(Vector2 position) {

	}

	@Override
	protected void updateVelocity(Vector2 position, Vector2 velocity) {

	}

	@Override
	protected Rectangle hitbox() {
		return getAnimations().getHitbox(BODY);
	}
}
