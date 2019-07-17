package com.mygdx.game.screens.game.ability;

public interface AbilityUseCondition {
	// should only accept 1 or 0 as input.
	boolean check(int using);
}
