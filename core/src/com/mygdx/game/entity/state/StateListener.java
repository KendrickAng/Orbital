package com.mygdx.game.entity.state;

public interface StateListener<S> {
	// This method is called when a state is added to States
	void stateAdd(S state);

	// This method is called when a state is removed from States
	void stateRemove(S state);
}
