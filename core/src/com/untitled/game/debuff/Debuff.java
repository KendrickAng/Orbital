package com.untitled.game.debuff;

/**
 * An effect on a LivingEntity over a duration of time.
 */
public class Debuff {
	private DebuffType type;

	private boolean inflicted;
	private float modifier;
	private float duration;

	private DebuffEnd debuffEnd;

	/**
	 * @param type     the type of debuff.
	 * @param modifier how strong the debuff is. (1f = 100%)
	 * @param duration the duration of the debuff.
	 */
	public Debuff(DebuffType type, float modifier, float duration) {
		this.type = type;
		this.modifier = modifier;
		this.duration = duration;
	}

	/**
	 * @param debuffEnd called when the debuff ends.
	 * @return this instance
	 */
	public Debuff defineDebuffEnd(DebuffEnd debuffEnd) {
		this.debuffEnd = debuffEnd;
		return this;
	}

	void begin() {
		inflicted = true;
	}

	void end() {
		inflicted = false;
		if (debuffEnd != null) {
			debuffEnd.call();
		}
	}

	/**
	 * @return whether this debuff is currently inflicted on the LivingEntity.
	 */
	public boolean isInflicted() {
		return inflicted;
	}

	/**
	 * @return the type of this debuff
	 */
	public DebuffType getType() {
		return type;
	}

	float getDuration() {
		return duration;
	}

	float getModifier() {
		return modifier;
	}
}
