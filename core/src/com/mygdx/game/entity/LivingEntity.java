package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.Animations;
import com.mygdx.game.GameScreen;
import com.mygdx.game.ability.Abilities;
import com.mygdx.game.entity.debuff.DebuffType;
import com.mygdx.game.entity.debuff.Debuffs;

import java.util.HashSet;

/**
 * An Entity which has:
 * - Abilities
 * - Debuffs
 * - Health
 * - Is Controllable
 */
public abstract class LivingEntity<T extends Enum<T>, R extends Enum<R>> extends Entity<T> {
	private float health;
	private float maxHealth;

	// Affected by key presses from input processor
	private Direction inputDirection;

	private HashSet<R> abilityStates;
	private Abilities<R> abilities;
	private Animations<R> abilityAnimations;
	private Debuffs<DebuffType> debuffs;

	public LivingEntity(GameScreen game) {
		super(game);

		this.health = health();
		this.maxHealth = health();

		this.inputDirection = Direction.NONE;
		this.abilityStates = new HashSet<R>();
		this.abilities = abilities();
		this.abilityAnimations = abilityAnimations();
		this.debuffs = debuffs();
	}

	protected abstract float health();

	protected abstract Abilities<R> abilities();

	protected abstract Animations<R> abilityAnimations();

	protected abstract Debuffs<DebuffType> debuffs();

	protected abstract void updateDirection(Direction inputDirection);

	/* Update */
	@Override
	public void update() {
		if (health <= 0) {
			dispose();
		}

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

	public void inflictDebuff(DebuffType type, float modifier, float duration) {
		debuffs.inflict(type, modifier, duration);
	}

	public void damage(float damage) {
		health -= damage;
		Gdx.app.log("LivingEntity.java", "HP: " + health);
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
