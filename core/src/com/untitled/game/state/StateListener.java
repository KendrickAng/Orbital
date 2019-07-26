package com.untitled.game.state;

/**
 * A StateListener should be added to a {@link States} instance.
 * The States instance will update all StateListeners about any new States.
 * StateListeners must also verify that the new State is valid.
 *
 * @param <S> the State enum
 */
public interface StateListener<S extends Enum> {
	/**
	 * This method is called before States changes state.
	 * All listeners must return true for States to change to this state.
	 *
	 * @param state the new State.
	 * @return whether the state is valid or not.
	 */
	boolean stateValid(S state);

	/**
	 * This method is called when States successfully changes state, after stateValid.
	 *
	 * @param state the new State
	 */
	void stateChange(S state);
}
