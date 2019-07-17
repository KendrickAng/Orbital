package com.mygdx.game.screens.game.debuff;

public class DebuffDefinition {
	private DebuffBegin begin;
	// called when you inflict a debuff AND when the debuff ends.
	private DebuffUpdate update;
	private DebuffEnd end;

	/* Calls */
	public void begin() {
		if (begin != null) {
			begin.call();
		}
	}

	public void update(float modifier) {
		if (update != null) {
			update.call(modifier);
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

	public DebuffDefinition defineUpdate(DebuffUpdate update) {
		this.update = update;
		return this;
	}

	public DebuffDefinition defineEnd(DebuffEnd end) {
		this.end = end;
		return this;
	}
}
