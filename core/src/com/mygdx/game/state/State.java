package com.mygdx.game.state;

public class State<T> {
	private StateGroup<T> group;
	public State(StateGroup<T> group) {
		this.group = group;
	}

	public StateGroup<T> group() {
		return group;
	}
}
