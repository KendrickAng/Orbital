package com.untitled.game.state;

import java.util.HashMap;
import java.util.HashSet;

/**
 * State manager.
 * Handles transitioning from one State to another given State an Input,
 * closely resembling a Finite State Machine.
 *
 * @param <I> the Input Enum
 * @param <S> the State Enum
 */
public class States<I extends Enum, S extends Enum> {
	private State<I, S> state;
	private HashMap<S, State<I, S>> states;
	private HashSet<StateListener<S>> listeners;

	public States() {
		this.states = new HashMap<>();
		this.listeners = new HashSet<>();
	}

	/**
	 * @param stateListener the StateListener will be updated when a new Input arrives.
	 */
	public void addListener(StateListener<S> stateListener) {
		listeners.add(stateListener);
	}

	/**
	 * Adds States to be managed by this State manager.
	 * The first State added will be the initial State.
	 *
	 * @param state the State
	 * @return this instance
	 */
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

	/**
	 * {@link StateListener}s will check if the given input is valid.
	 * Transition to the correct State if valid,
	 * then update all StateListeners that the State has changed.
	 *
	 * @param input the Input
	 * @return this instance
	 */
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

	/**
	 * Calls {@link StateUpdate} of the current State.
	 */
	public void update() {
		this.state.update();
	}
}
