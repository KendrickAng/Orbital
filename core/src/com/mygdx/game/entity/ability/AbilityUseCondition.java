package com.mygdx.game.entity.ability;

public interface AbilityUseCondition {
	// should only accept 1 or 0 as input.
	boolean check(int using);
}
