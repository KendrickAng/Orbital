package com.mygdx.game.screens.game.state;

import java.util.HashMap;
import java.util.HashSet;

public class States<I extends Enum, S extends Enum> {
	private State<I, S> state;
	private HashMap<S, State<I, S>> states;
	private HashSet<StateListener<S>> listeners;

	public States() {
		this.states = new HashMap<>();
		this.listeners = new HashSet<>();
	}

	public void addListener(StateListener<S> stateListener) {
		listeners.add(stateListener);
	}

	public States<I, S> add(State<I, S> state) {
		if (this.state == null) {
			this.state = state;
			for (StateListener<S> listener : listeners) {
				listener.stateChange(state.getName());
			}
		}

		states.put(state.getName(), state);
		return this;
	}

	public boolean input(I input) {
		S name = this.state.getEdge(input);
		if (name == null) {
			return false;
		}

		State<I, S> toState = states.get(name);
		if (toState == null) {
			return false;
		}

		for (StateListener<S> listener : listeners) {
			if (!listener.stateValid(toState.getName())) {
				return false;
			}
		}

		this.state.end();
		this.state = toState;
		this.state.begin();

		for (StateListener<S> listener : listeners) {
			listener.stateChange(toState.getName());
		}

		return true;
	}

	public void update() {
		this.state.update();
	}
}
