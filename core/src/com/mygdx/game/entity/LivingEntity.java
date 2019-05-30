package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.ability.Abilities;
import com.mygdx.game.entity.debuff.DebuffType;
import com.mygdx.game.entity.debuff.Debuffs;

/**
 * An Entity which has:
 * - Abilities
 * - Debuffs
 * - Health
 * - Is Controllable
 */
public abstract class LivingEntity<T extends Enum, R extends Enum> extends Entity<T, R> {
	private float health;
	private float maxHealth;

	// Affected by key presses from input processor
	private Direction inputDirection;

	private Abilities<T> abilities;
	private Debuffs<DebuffType> debuffs;

	public LivingEntity(GameScreen game) {
		super(game);

		this.health = health();
		this.maxHealth = health();

		this.inputDirection = Direction.NONE;
		this.abilities = new Abilities<>();
		this.debuffs = new Debuffs<>();

		defineAbilities(abilities);
		defineDebuffs(debuffs);
	}

	protected abstract float health();

	protected abstract void defineAbilities(Abilities<T> abilities);

	protected abstract void defineDebuffs(Debuffs<DebuffType> debuffs);

	protected abstract void updateDirection(Direction inputDirection);

	/* Update */
	@Override
	public void update() {
		if (health <= 0) {
			dispose();
		}

		abilities.update();
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
	Abilities<T> getAbilities() {
		return abilities;
	}

	public Direction getInputDirection() {
		return inputDirection;
	}
}
