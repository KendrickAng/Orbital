package com.mygdx.game.screens.game.state;

public interface StateListener<S> {
	// This method is called before States changes state.
	// All listeners must return true for States to change to this state.
	boolean stateValid(S state);

	// This method is called when States changes state.
	void stateChange(S state);
}
