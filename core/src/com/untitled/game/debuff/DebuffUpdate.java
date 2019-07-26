package com.untitled.game.debuff;

/**
 * A Consumer.
 */
public interface DebuffUpdate {
	/**
	 * @param modifier the stacked modifier of debuffs with the same DebuffType.
	 */
	void call(float modifier);
}
