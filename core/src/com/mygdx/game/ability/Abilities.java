package com.mygdx.game.ability;

import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.state.State;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * What abilities an entity can use.
 */
public class Abilities<T> {
	private Timer timer;

	// Abilities defined
	private HashMap<State<T>, Ability> abilities;

	// Abilities that can be used
	private HashMap<State<T>, Ability> usable;

	// Abilities on cooldown
	private HashSet<State<T>> unusable;

	// Cooldowns
	private HashMap<State<T>, Timer.Task> cooldowns;

	public Abilities() {
		timer = new Timer();
		abilities = new HashMap<State<T>, Ability>();
		usable = new HashMap<State<T>, Ability>();
		unusable = new HashSet<State<T>>();
		cooldowns = new HashMap<State<T>, Timer.Task>();
	}

	public Abilities<T> add(State<T> state, Ability ability) {
		abilities.put(state, ability);
		usable.put(state, ability);
		cooldowns.put(state, null);
		return this;
	}

	public void use(final State<T> state, final Callback abilityDone) {
		if (ready(state)) {
			final Ability ability = usable.get(state);

			// Ability executing
			timer.scheduleTask(new Timer.Task() {
				@Override
				public void run() {
					// Ability duration is up
					abilityDone.call();

					// Cooldown
					cooldowns.put(state, timer.scheduleTask(new Timer.Task() {
						@Override
						public void run() {
							cooldowns.put(state, null);
						}
					}, ability.getCooldown()));
					unusable.add(state);
				}
			}, ability.getDuration());

			// Ability used, set ability slot to empty
			usable.put(state, null);
		}
	}

	public void update() {
		Iterator<State<T>> iterator = unusable.iterator();
		while (iterator.hasNext()) {
			State<T> state = iterator.next();
			Ability ability = abilities.get(state);
			// Check if ability is ready to be removed from cooldown
			boolean isOnCooldown = cooldowns.get(state) != null;
			if (ability.isReady(isOnCooldown)) {
				if (isOnCooldown) {
					// Cancel task if still running
					cooldowns.get(state).cancel();
				}
				// Put original ability back in ability slot
				usable.put(state, abilities.get(state));
				iterator.remove();
			}
		}
	}

	public boolean ready(State<T> state) {
		return usable.get(state) != null;
	}

	// TODO: Reset abilities when switching characters
}
