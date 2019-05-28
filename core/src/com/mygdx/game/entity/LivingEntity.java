package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.ability.Abilities;

/**
 * An Entity which has:
 * - Abilities
 * - Debuffs
 * - Health
 * - Is Controllable
 */
public abstract class LivingEntity<T> extends Entity<T> {
	// Affected by key presses from input processor
	private Direction inputDirection;

	private Abilities<T> abilities;
	// TODO: Effects/Debuffs Class (Crowd Control)

	public LivingEntity(MyGdxGame game) {
		super(game);
		this.inputDirection = Direction.NONE;
		this.abilities = abilities();
	}

	protected abstract Abilities<T> abilities();

	/* Render */
	@Override
	public void render(SpriteBatch batch) {
		super.render(batch);
		abilities.update();
	}

	/* Called from InputProcessor */
	public void setInputDirection(Direction direction) {
		inputDirection = direction;
	}

	/* Getters */
	Abilities<T> getAbilities() {
		return abilities;
	}

	public Direction getInputDirection() {
		return inputDirection;
	}
}
