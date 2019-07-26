package com.untitled.game.ability;

/**
 * A Consumer.
 *
 * @param <S> a State enum.
 */
public interface AbilityBegin<S extends Enum> {
	/**
	 * @param state the State that begun the ability.
	 */
	void begin(S state);
}
