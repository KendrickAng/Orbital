package com.mygdx.game.entity.state;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class States<S extends Enum> {
	private S state;
	private HashMap<S, HashSet<S>> states;
	private HashSet<StateListener<S>> listeners;

	public States() {
		this.states = new HashMap<>();
		this.listeners = new HashSet<>();
	}

	public void addListener(StateListener<S> stateListener) {
		listeners.add(stateListener);
	}

	public States<S> mapState(S state, List<S> edges) {
		states.put(state, new HashSet<>(edges));
		return this;
	}

	public void setState(S state) {
		if (this.state == null) {
			this.state = state;

		} else if (states.get(this.state).contains(state)) {
			for (StateListener<S> listener : listeners) {
				if (!listener.stateValid(state)) {
					return;
				}
			}

			this.state = state;

			for (StateListener<S> listener : listeners) {
				listener.stateChange(state);
			}
		}
	}

	public S getState() {
		return state;
	}
}
