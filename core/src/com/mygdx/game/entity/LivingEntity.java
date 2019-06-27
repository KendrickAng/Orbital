package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.ability.Abilities;
import com.mygdx.game.entity.debuff.DebuffType;
import com.mygdx.game.entity.debuff.Debuffs;

/**
 * An Entity which has:
 * - Health
 * - Abilities
 * - Debuffs
 */
public abstract class LivingEntity<T extends Enum, R extends Enum> extends Entity<T, R> {
	private float health;
	private float maxHealth;

	private Abilities<T> abilities;
	private Debuffs<DebuffType> debuffs;

	public LivingEntity(GameScreen game) {
		super(game);

		this.health = health();
		this.maxHealth = health();

		this.abilities = new Abilities<>();
		this.debuffs = new Debuffs<>();

		addStateListener(abilities);
		defineAbilities(abilities);
		defineDebuffs(debuffs);
	}

	protected abstract float health();

	// creates new instances of Ability for primary, secondary and tertiary and maps the corrs CharacterState enum to Ability.
	protected abstract void defineAbilities(Abilities<T> abilities);

	// called when an instance of LivingEntity is created.
	protected abstract void defineDebuffs(Debuffs<DebuffType> debuffs);

	/* Update */
	@Override
	public void update() {
		if (health <= 0) {
			dispose();
		}

		abilities.update();
	}

	/* Setters */
	public void inflictDebuff(DebuffType type, float modifier, float duration) {
		debuffs.inflict(type, modifier, duration);
	}

	public void damage(float damage) {
		health -= damage;
		Gdx.app.log("LivingEntity.java", "HP: " + health);
	}
}
