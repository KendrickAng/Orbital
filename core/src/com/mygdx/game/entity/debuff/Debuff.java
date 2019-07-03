package com.mygdx.game.entity.debuff;

public class Debuff {
	private DebuffType type;
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

	public void end() {
		if (debuffEnd != null) {
			debuffEnd.call();
		}
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
