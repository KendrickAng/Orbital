package com.mygdx.game.entity.debuff;

public class DebuffDefinition {
	private DebuffBegin begin;
	// called when you inflict a debuff AND when the debuff ends.
	private DebuffCallback apply;
	private DebuffEnd end;

	public DebuffDefinition() {
		begin = () -> {
		};
		end = () -> {
		};
		apply = modifier -> {
		};
	}

	/* Calls */
	public void begin() {
		begin.call();
	}

	public void apply(float modifier) {
		apply.call(modifier);
	}

	public void end() {
		end.call();
	}

	/* Setters */
	public DebuffDefinition setBegin(DebuffBegin begin) {
		this.begin = begin;
		return this;
	}

	public DebuffDefinition setApply(DebuffCallback apply) {
		this.apply = apply;
		return this;
	}

	public DebuffDefinition setEnd(DebuffEnd end) {
		this.end = end;
		return this;
	}
}
