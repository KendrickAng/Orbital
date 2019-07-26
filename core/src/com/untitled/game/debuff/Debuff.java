package com.untitled.game.debuff;

public class Debuff {
	private DebuffType type;

	private boolean inflicted;
	private float modifier;
	private float duration;

	private DebuffEnd debuffEnd;

	public Debuff(DebuffType type, float modifier, float duration) {
		this.type = type;
		this.modifier = modifier;
		this.duration = duration;
	}

	public Debuff defineDebuffEnd(DebuffEnd debuffEnd) {
		this.debuffEnd = debuffEnd;
		return this;
	}

	public void begin() {
		inflicted = true;
	}

	public void end() {
		inflicted = false;
		if (debuffEnd != null) {
			debuffEnd.call();
		}
	}

	public boolean isInflicted() {
		return inflicted;
	}

	public DebuffType getType() {
		return type;
	}

	public float getDuration() {
		return duration;
	}

	public float getModifier() {
		return modifier;
	}
}
