package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.entity.ability.Abilities;
import com.mygdx.game.entity.ability.Ability;
import com.mygdx.game.entity.debuff.Debuff;
import com.mygdx.game.entity.debuff.DebuffDefinition;
import com.mygdx.game.entity.debuff.Debuffs;
import com.mygdx.game.screens.GameScreen;

import java.util.HashSet;

import static com.mygdx.game.assets.TextureName.STUNNED;
import static com.mygdx.game.assets.TextureName.WEAK_SPOT;
import static com.mygdx.game.entity.debuff.DebuffType.DAMAGE_REDUCTION;
import static com.mygdx.game.entity.debuff.DebuffType.DAMAGE_REFLECT;
import static com.mygdx.game.entity.debuff.DebuffType.STUN;
import static com.mygdx.game.entity.debuff.DebuffType.WEAK;

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

	private Abilities<S> abilities;

	private Debuffs debuffs;
	private HashSet<Sprite> inflictedDebuffs;

	private Timer timer;
	private float damageReduction;
	private float damageReflect;

	private boolean stunned;
	private Sprite stunnedSprite;
	private boolean weak;
	private Sprite weakSprite;

	private boolean damaged;

	public LivingEntity(GameScreen game, int renderPriority) {
		super(game, renderPriority);

		this.health = health();
		this.maxHealth = health();

		this.abilities = new Abilities<>();
		this.inflictedDebuffs = new HashSet<>();
		this.debuffs = new Debuffs();

		this.stunnedSprite = new Sprite(game.getAssets().getTexture(STUNNED));
		this.weakSprite = new Sprite(game.getAssets().getTexture(WEAK_SPOT));

		addStateListener(abilities);
		defineAbilities(abilities);
		defineDebuffs(debuffs);

		setRenderTask((batch) -> {
			float spriteWidth = 20;
			float width = inflictedDebuffs.size() * spriteWidth;
			float x = getMiddleX() - width / 2;
			for (Sprite sprite : inflictedDebuffs) {
				sprite.setPosition(x, getTopY());
				sprite.draw(batch);
				x += spriteWidth;
			}
		});

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
							inflictedDebuffs.add(stunnedSprite);
						})
						.defineEnd(() -> {
							stunned = false;
							inflictedDebuffs.remove(stunnedSprite);
							endCrowdControl();
						}))

				.map(WEAK, new DebuffDefinition()
						.defineBegin(() -> {
							weak = true;
							inflictedDebuffs.add(weakSprite);
						})
						.defineEnd(() -> {
							weak = false;
							inflictedDebuffs.remove(weakSprite);
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

	// Abstract method that is called when entity is damaged
	protected abstract void damage();

	// Abstract method that is called when entity is debuffed
	protected abstract void debuff(Debuff debuff);

	public void inflictDebuff(Debuff debuff) {
		debuff(debuff);
		debuffs.inflict(debuff);
	}

	public void cancelDebuff(Debuff debuff) {
		debuffs.cancel(debuff);
	}

	public void heal(float health) {
		this.health += health;
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

	public boolean isWeak() {
		return weak;
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

	public Ability[] getAbilities() {
		return abilities.getAbilities();
	}
}
