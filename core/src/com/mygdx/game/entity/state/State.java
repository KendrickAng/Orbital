package com.mygdx.game.entity.state;

import java.util.HashMap;

public class State<I, S> {
	private S name;
	private StateBegin stateBegin;
	private StateUpdate stateUpdate;
	private StateEnd stateEnd;
	private HashMap<I, S> edges;

	public State(S name) {
		this.name = name;
		edges = new HashMap<>();
	}

	public State<I, S> defineBegin(StateBegin begin) {
		this.stateBegin = begin;
		return this;
	}

	public State<I, S> defineUpdate(StateUpdate update) {
		this.stateUpdate = update;
		return this;
	}

	public State<I, S> defineEnd(StateEnd end) {
		this.stateEnd = end;
		return this;
	}

	public State<I, S> addEdge(I input, S state) {
		edges.put(input, state);
		return this;
	}

	public void begin() {
		if (stateBegin != null) {
			stateBegin.begin();
		}
	}

	public void update() {
		if (stateUpdate != null) {
			stateUpdate.update();
		}
	}

	public void end() {
		if (stateEnd != null) {
			stateEnd.end();
		}
	}

	public S getEdge(I input) {
		return edges.get(input);
	}

	public S getName() {
		return name;
	}
}
