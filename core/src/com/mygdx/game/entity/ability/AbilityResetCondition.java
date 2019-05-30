package com.mygdx.game.entity.ability;

public interface AbilityResetCondition {
	boolean check(boolean isOnCooldown);
}
