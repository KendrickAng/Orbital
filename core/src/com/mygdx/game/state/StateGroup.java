package com.mygdx.game.state;

import java.util.HashSet;

public class StateGroup<T> {
	private HashSet<State> states;

	public StateGroup() {
		states = new HashSet<State>();
	}

	public State<T> add() {
		State<T> state = new State<T>(this);
		states.add(state);
		return state;
	}

	public boolean contains(State state) {
		return states.contains(state);
	}

	public State[] toArray() {
		return states.toArray(new State[0]);
	}
}
