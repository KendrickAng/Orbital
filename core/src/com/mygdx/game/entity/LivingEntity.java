package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.ability.Abilities;
import com.mygdx.game.entity.debuff.Debuff;
import com.mygdx.game.entity.debuff.Debuffs;

/**
 * An Entity which has:
 * - Health
 * - Abilities
 * - Debuffs
 */
public abstract class LivingEntity<I extends Enum, S extends Enum, P extends Enum> extends Entity<I, S, P> {
	private float health;
	private float maxHealth;

	private Debuffs debuffs;

	public LivingEntity(GameScreen game) {
		super(game);

		this.health = health();
		this.maxHealth = health();

		Abilities<S> abilities = new Abilities<>();
		this.debuffs = new Debuffs();

		addStateListener(abilities);
		defineAbilities(abilities);
		defineDebuffs(debuffs);
	}

	protected abstract float health();

	// creates new instances of Ability for primary, secondary and tertiary and maps the corrs CharacterState enum to Ability.
	protected abstract void defineAbilities(Abilities<S> abilities);

	// called when an instance of LivingEntity is created.
	protected abstract void defineDebuffs(Debuffs debuffs);

	/* Update */
	@Override
	public void update() {
		if (health <= 0) {
			dispose();
		}
	}

	public void inflictDebuff(Debuff debuff) {
		debuffs.inflict(debuff);
	}

	public void cancelDebuff(Debuff debuff) {
		debuffs.cancel(debuff);
	}

	public void damage(float damage) {
		health -= damage;
		Gdx.app.log("LivingEntity.java", "HP: " + health);
	}

	public float getMaxHealth() {
		return maxHealth;
	}
}
