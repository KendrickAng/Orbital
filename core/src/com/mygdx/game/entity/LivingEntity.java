package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.ability.Abilities;
import com.mygdx.game.entity.debuff.Debuff;
import com.mygdx.game.entity.debuff.DebuffDefinition;
import com.mygdx.game.entity.debuff.Debuffs;

import static com.mygdx.game.entity.debuff.DebuffType.DAMAGE_REDUCTION;

/**
 * An Entity which has:
 * - Health
 * - Abilities
 * - Debuffs
 */
public abstract class LivingEntity<I extends Enum, S extends Enum, P extends Enum> extends Entity<I, S, P> {
	private static final float DAMAGE_BLINK_DURATION = 0.25f;
	private static final float DAMAGE_DEBUFF_DURATION = 1f;

	private float health;
	private float maxHealth;

	private Debuffs debuffs;

	private Timer timer;
	private float damageReduction;

	public LivingEntity(GameScreen game) {
		super(game);

		this.health = health();
		this.maxHealth = health();

		Abilities<S> abilities = new Abilities<>();
		this.debuffs = new Debuffs();

		addStateListener(abilities);
		defineAbilities(abilities);
		defineDebuffs(debuffs);

		this.timer = new Timer();
		debuffs.map(DAMAGE_REDUCTION, new DebuffDefinition()
				.defineApply(modifier -> {
					if (modifier > 1) {
						modifier = 1;
					}
					this.damageReduction = modifier;
				}));
	}

	protected abstract float health();

	// creates new instances of Ability for primary, secondary and tertiary and maps the corrs CharacterState enum to Ability.
	protected abstract void defineAbilities(Abilities<S> abilities);

	// called when an instance of LivingEntity is created.
	protected abstract void defineDebuffs(Debuffs debuffs);

	public void inflictDebuff(Debuff debuff) {
		debuffs.inflict(debuff);
	}

	public void cancelDebuff(Debuff debuff) {
		debuffs.cancel(debuff);
	}

	public void inflictDamage(float damage) {
		if (damageReduction < 1) {
			health -= damage * (1 - damageReduction);
			getColor().set(1, 0, 0, 1);

			inflictDebuff(new Debuff(DAMAGE_REDUCTION, 1f, DAMAGE_DEBUFF_DURATION));
			timer.scheduleTask(new Timer.Task() {
				@Override
				public void run() {
					getColor().set(1, 1, 1, 1);
				}
			}, DAMAGE_BLINK_DURATION);

			Gdx.app.log("LivingEntity.java", "HP: " + health);
			if (health <= 0) {
				dispose();
			}
		}
	}

	public float getHealth() {
		return health;
	}

	public float getMaxHealth() {
		return maxHealth;
	}
}
