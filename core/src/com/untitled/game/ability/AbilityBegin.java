package com.untitled.game.ability;

/**
 * A Consumer.
 *
 * @param <S> the State enum that begun the ability.
 */
public interface AbilityBegin<S> {
	void begin(S state);
}
