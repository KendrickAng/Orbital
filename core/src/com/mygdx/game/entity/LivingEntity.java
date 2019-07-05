package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.entity.ability.Abilities;
import com.mygdx.game.entity.debuff.Debuff;
import com.mygdx.game.entity.debuff.DebuffDefinition;
import com.mygdx.game.entity.debuff.Debuffs;
import com.mygdx.game.screens.GameScreen;

import static com.mygdx.game.assets.Assets.TextureName.STUNNED;
import static com.mygdx.game.entity.debuff.DebuffType.DAMAGE_REDUCTION;
import static com.mygdx.game.entity.debuff.DebuffType.DAMAGE_REFLECT;
import static com.mygdx.game.entity.debuff.DebuffType.STUN;

/**
 * An Entity which has:
 * - Health
 * - Abilities
 * - Debuffs
 */
public abstract class LivingEntity<I extends Enum, S extends Enum, P extends Enum> extends Entity<I, S, P> {
	private static final float DAMAGE_BLINK_DURATION = 0.25f;

	private float health;
	private float maxHealth;

	private Sprite debuff;
	private Debuffs debuffs;

	private Timer timer;
	private float damageReduction;
	private float damageReflect;
	private boolean stunned;
	private boolean damaged;

	public LivingEntity(GameScreen game, int renderPriority) {
		super(game, renderPriority);

		this.health = health();
		this.maxHealth = health();

		Abilities<S> abilities = new Abilities<>();
		this.debuffs = new Debuffs();

		addStateListener(abilities);
		defineAbilities(abilities);
		defineDebuffs(debuffs);

		this.timer = new Timer();
		debuffs.map(DAMAGE_REDUCTION, new DebuffDefinition()
				.defineUpdate(modifier -> {
					// Can't reduce damage more than 100%.
					if (modifier > 1) {
						modifier = 1;
					}
					this.damageReduction = modifier;
				}))

				.map(DAMAGE_REFLECT, new DebuffDefinition()
						.defineUpdate(modifier -> {
							// Can't reflect more damage than recieved.
							if (modifier > 1) {
								modifier = 1;
							}
							this.damageReflect = modifier;
						}))

				.map(STUN, new DebuffDefinition()
						.defineBegin(() -> {
							beginCrowdControl();
							stunned = true;
							debuff = new Sprite(game.getAssets().getTexture(STUNNED));
							setRenderTask((batch) -> {
								debuff.setPosition(getMiddleX() - debuff.getWidth() / 2, getTopY());
								debuff.draw(batch);
							});
						})
						.defineEnd(() -> {
							stunned = false;
							endCrowdControl();
							setRenderTask(null);
						}));
	}

	protected abstract float health();

	protected abstract float damagedDuration();

	// creates new instances of Ability for primary, secondary and tertiary and maps the corrs CharacterState enum to Ability.
	protected abstract void defineAbilities(Abilities<S> abilities);

	// called when an instance of LivingEntity is created.
	protected abstract void defineDebuffs(Debuffs debuffs);

	// Called when crowd control is first inflicted on the entity.
	protected abstract void beginCrowdControl();

	// Called when entity has no more crowd control.
	public abstract void endCrowdControl();

	public abstract float getMiddleX();

	protected abstract float getTopY();

	protected abstract void damage();

	public void inflictDebuff(Debuff debuff) {
		debuffs.inflict(debuff);
	}

	public void cancelDebuff(Debuff debuff) {
		debuffs.cancel(debuff);
	}

	public boolean inflictDamage(LivingEntity entity, float damage) {
		if (!damaged) {
			damaged = true;
			timer.scheduleTask(new Timer.Task() {
				@Override
				public void run() {
					damaged = false;
				}
			}, damagedDuration());

			// Abstract method to check if entity is damaged
			damage();

			if (entity != null && damageReflect > 0) {
				entity.inflictTrueDamage(damage * damageReflect);
			}

			if (damageReduction < 1) {
				health -= damage * (1 - damageReduction);
				getColor().set(1, 0, 0, 1);
				timer.scheduleTask(new Timer.Task() {
					@Override
					public void run() {
						getColor().set(1, 1, 1, 1);
					}
				}, DAMAGE_BLINK_DURATION);

//				Gdx.app.log("LivingEntity.java", "HP: " + health);
				if (health <= 0) {
					dispose();
				}

				return true;
			} else {
				// 100% damage reduction.
				return false;
			}
		}
		return false;
	}

	// Ignore damageReduction, damageReflect
	public void inflictTrueDamage(float damage) {
		health -= damage;
		getColor().set(1, 0, 0, 1);
		timer.scheduleTask(new Timer.Task() {
			@Override
			public void run() {
				getColor().set(1, 1, 1, 1);
			}
		}, DAMAGE_BLINK_DURATION);

		if (health <= 0) {
			dispose();
		}
	}

	public boolean isStunned() {
		return stunned;
	}

	public boolean isCrowdControl() {
		return stunned;
	}

	protected void clearCrowdControl() {
		debuffs.cancel(STUN);
	}

	/* Getters */
	public float getHealth() {
		return health;
	}

	public float getMaxHealth() {
		return maxHealth;
	}
}
