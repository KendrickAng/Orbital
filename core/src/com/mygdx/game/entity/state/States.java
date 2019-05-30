package com.mygdx.game.entity.state;

import java.util.HashSet;

public class States<S extends Enum> {
	private HashSet<S> states;
	private HashSet<StateListener<S>> listeners;

	public States() {
		this.states = new HashSet<>();
		this.listeners = new HashSet<>();
	}

	public void addListener(StateListener<S> stateListener) {
		listeners.add(stateListener);
	}

	public void addState(S state) {
		states.add(state);
		for (StateListener<S> listener : listeners) {
			listener.stateAdd(state);
		}
	}

	public void removeState(S state) {
		states.remove(state);
		for (StateListener<S> listener : listeners) {
			listener.stateRemove(state);
		}
	}
}
