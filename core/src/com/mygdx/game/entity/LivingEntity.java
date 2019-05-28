package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.Animations;
import com.mygdx.game.GameScreen;
import com.mygdx.game.ability.Abilities;

import java.util.HashSet;

/**
 * An Entity which has:
 * - Abilities
 * - Debuffs
 * - Health
 * - Is Controllable
 */
public abstract class LivingEntity<T extends Enum<T>, R extends Enum<R>> extends Entity<T> {
	// Affected by key presses from input processor
	private Direction inputDirection;

	private HashSet<R> abilityStates;
	private Abilities<R> abilities;
	private Animations<R> abilityAnimations;
	// TODO: Effects/Debuffs Class (Crowd Control)

	public LivingEntity(GameScreen game) {
		super(game);
		this.inputDirection = Direction.NONE;
		this.abilityStates = new HashSet<R>();
		this.abilities = abilities();
		this.abilityAnimations = abilityAnimations();
	}

	protected abstract Abilities<R> abilities();
	protected abstract Animations<R> abilityAnimations();
	protected abstract void updateDirection(Direction inputDirection);

	/* Update */
	@Override
	public void update() {
		abilities.update();
	}

	@Override
	public Sprite updateAnimation() {
		// Ideally, the animation should be able to display a character walking & doing something else at the same time.
		if (abilityAnimations == null || abilityStates.isEmpty()) {
			return getBasicAnimations().from(getBasicState());
		} else {
			return abilityAnimations.from(abilityStates);
		}
	}

	/* Setters */
	public void setInputDirection(Direction direction) {
		inputDirection = direction;
		updateDirection(inputDirection);
	}

	/* Getters */
	public HashSet<R> getAbilityStates() {
		return abilityStates;
	}

	Abilities<R> getAbilities() {
		return abilities;
	}

	public Direction getInputDirection() {
		return inputDirection;
	}
}
