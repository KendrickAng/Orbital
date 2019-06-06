package com.mygdx.game.entity.debuff;

public class Debuff {
	private DebuffBegin begin;
	// called when you inflict a debuff AND when the debuff ends.
	private DebuffCallback apply;
	private DebuffEnd end;

	public Debuff() {
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
	public Debuff setBegin(DebuffBegin begin) {
		this.begin = begin;
		return this;
	}

	public Debuff setApply(DebuffCallback apply) {
		this.apply = apply;
		return this;
	}

	public Debuff setEnd(DebuffEnd end) {
		this.end = end;
		return this;
	}
}
