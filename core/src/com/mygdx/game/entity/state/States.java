package com.mygdx.game.entity.state;

import com.badlogic.gdx.utils.Timer;

import java.util.HashSet;

public class States<S extends Enum> {
	private Timer timer;
	private HashSet<StateListener<S>> listeners;

	public States() {
		this.timer = new Timer();
		this.listeners = new HashSet<>();
	}

	public void addListener(StateListener<S> stateListener) {
		listeners.add(stateListener);
	}

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

	public void removeState(S state) {
		for (StateListener<S> listener : listeners) {
			listener.stateRemove(state);
		}
	}

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
