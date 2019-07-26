package com.untitled.game.state;

import java.util.HashMap;

/**
 * A State which can transition to other States, based on a certain Input.
 * Closely relates to a Finite State Machine.
 *
 * @param <I> an Input enum
 * @param <S> a State enum
 */
public class State<I extends Enum, S extends Enum> {
	private S name;
	private StateBegin stateBegin;
	private StateUpdate stateUpdate;
	private StateEnd stateEnd;
	private HashMap<I, S> edges;

	public State(S name) {
		this.name = name;
		edges = new HashMap<>();
	}

	/**
	 * @param begin Called when this state has just been transitioned to.
	 * @return this instance
	 */
	public State<I, S> defineBegin(StateBegin begin) {
		this.stateBegin = begin;
		return this;
	}

	/**
	 * @param update Called when this state is active.
	 * @return this instance
	 */
	public State<I, S> defineUpdate(StateUpdate update) {
		this.stateUpdate = update;
		return this;
	}

	/**
	 * @param end Called when leaving this state.
	 * @return this instance
	 */
	public State<I, S> defineEnd(StateEnd end) {
		this.stateEnd = end;
		return this;
	}

	/**
	 * Add a State neighbour which this State can transition to with a given Input.
	 *
	 * @param input the Input
	 * @param state the State neighbour
	 * @return this instance
	 */
	public State<I, S> addEdge(I input, S state) {
		edges.put(input, state);
		return this;
	}

	void begin() {
		if (stateBegin != null) {
			stateBegin.begin();
		}
	}

	void update() {
		if (stateUpdate != null) {
			stateUpdate.update();
		}
	}

	void end() {
		if (stateEnd != null) {
			stateEnd.end();
		}
	}

	S getEdge(I input) {
		return edges.get(input);
	}

	S getName() {
		return name;
	}
}
