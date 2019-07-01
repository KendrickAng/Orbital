package com.mygdx.game.entity.debuff;

public class DebuffDefinition {
	private DebuffBegin begin;
	// called when you inflict a debuff AND when the debuff ends.
	private DebuffCallback apply;
	private DebuffEnd end;

	/* Calls */
	public void begin() {
		if (begin != null) {
			begin.call();
		}
	}

	public void apply(float modifier) {
		if (apply != null) {
			apply.call(modifier);
		}
	}

	public void end() {
		if (end != null) {
			end.call();
		}
	}

	/* Setters */
	public DebuffDefinition defineBegin(DebuffBegin begin) {
		this.begin = begin;
		return this;
	}

	public DebuffDefinition defineApply(DebuffCallback apply) {
		this.apply = apply;
		return this;
	}

	public DebuffDefinition defineEnd(DebuffEnd end) {
		this.end = end;
		return this;
	}
}
