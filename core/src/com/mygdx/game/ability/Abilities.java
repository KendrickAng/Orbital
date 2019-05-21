package com.mygdx.game.ability;

import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.state.State;

import java.util.HashMap;

/**
 * What abilities an entity can use.
 */
public class Abilities<T> {
	// TODO: Learn about Timer class. TimeUtils was not really a good class to use.
	private Timer timer;
	private HashMap<State<T>, Timer.Task> tasks;

	private HashMap<State<T>, Float> cooldowns;
	private HashMap<State<T>, Float> durations;

	public Abilities() {
		timer = new Timer();
		tasks = new HashMap<State<T>, Timer.Task>();

		cooldowns = new HashMap<State<T>, Float>();
		durations = new HashMap<State<T>, Float>();
	}

	public Abilities<T> add(State<T> state, float cooldown, float duration) {
		cooldowns.put(state, cooldown);
		durations.put(state, duration);
		return this;
	}

	public void use(final State<T> state, final Callback callback) {
		tasks.put(state, timer.scheduleTask(new Timer.Task() {
			@Override
			public void run() {
				timer.scheduleTask(new Timer.Task() {
					@Override
					public void run() {
						tasks.remove(state);
					}
				}, cooldowns.get(state));
				callback.call();
			}
		}, durations.get(state)));
	}

	public boolean ready(State<T> state) {
		return !tasks.containsKey(state);
	}
}
