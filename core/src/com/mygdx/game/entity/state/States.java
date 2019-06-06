package com.mygdx.game.entity.state;

import com.badlogic.gdx.utils.Timer;

import java.util.HashSet;

public class States<S extends Enum> {
	private Timer timer;
	// a StateListener is added when Entity's addStateListener is added.
	private HashSet<StateListener<S>> listeners;

	public States() {
		this.timer = new Timer();
		this.listeners = new HashSet<>();
	}

	public void addListener(StateListener<S> stateListener) {
		listeners.add(stateListener);
	}

	// only add the state to all listeners if they are ALL compatible.
	public boolean addState(S state) {
		for (StateListener<S> listener : listeners) {
			if (!listener.stateAddValid(state)) {
				return false;
			}
		}

		for (StateListener<S> listener : listeners) {
			listener.stateAdd(state);
		}

		return true;
	}

	// remove the state from ALL listeners
	public void removeState(S state) {
		for (StateListener<S> listener : listeners) {
			listener.stateRemove(state);
		}
	}

	// scheduleState() -> addState() -> stateAddValid forAll listeners -> stateAdd() forAll listeners -> removeState() -> stateRemove()
	public void scheduleState(S state, float duration) {
		if (addState(state)) {
			timer.scheduleTask(new Timer.Task() {
				@Override
				public void run() {
					removeState(state);
				}
			}, duration);
		}
	}
}
