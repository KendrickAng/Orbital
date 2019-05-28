package com.mygdx.game.state;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * What action(s) an entity is undertaking.
 */
public class States<T> {
	private HashSet<State<T>> states;

	public States() {
		states = new HashSet<State<T>>();
	}

	public States<T> add(State<T> state) {
		// ensures that no two same states from the same group exists at same time
		for (State s : states) {
			if (state.group().contains(s)) {
				states.remove(s);
			}
		}
		states.add(state);
		return this;
	}

	public void remove(State state) {
		states.remove(state);
	}

	public void remove(StateGroup group) {
		for (State s : group.toArray()) {
			states.remove(s);
		}
	}

	public boolean contains(State state) {
		return states.contains(state);
	}

	public boolean contains(StateGroup group) {
		for (State s : group.toArray()) {
			if (states.contains(s)) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<State<T>> toArray() {
		return new ArrayList<State<T>>(states);
	}
}
