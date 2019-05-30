package com.mygdx.game.ability;

import com.badlogic.gdx.utils.Timer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * What abilities an entity can use.
 * This class will also mutate the states of the entity accordingly.
 */
public class Abilities<T> {
	private Timer timer;

	private HashSet<T> states;

	// Abilities defined
	private HashMap<T, Ability> abilities;

	// Abilities that can be used
	private HashMap<T, Ability> usable;

	// Abilities that are currently active
	private HashMap<T, Timer.Task> using;

	// Abilities that cannot be used and need to be checked if they can be reset
	private HashMap<T, Timer.Task> unusable;

	public Abilities(HashSet<T> states) {
		this.timer = new Timer();
		this.states = states;

		abilities = new HashMap<>();
		usable = new HashMap<>();
		using = new HashMap<>();
		unusable = new HashMap<>();
	}

	/* Add an ability */
	public Abilities<T> add(T state, Ability ability) {
		abilities.put(state, ability);
		usable.put(state, ability);
		return this;
	}

	/* Use an ability */
	public void use(final T state) {
		final Ability ability = usable.get(state);

		// Check if ability can be used
		if (ability != null && ability.canUse(using.size())) {
			// Schedule a task for when ability ends
			Timer.Task endTask = timer.scheduleTask(new Timer.Task() {
				@Override
				public void run() {
					// Schedule a task for when ability goes off cooldown
					Timer.Task cooldownTask = timer.scheduleTask(new Timer.Task() {
						@Override
						public void run() {
							// Set task to null
							unusable.put(state, null);
						}
					}, ability.getCooldown());

					// Call ability's end callback
					ability.end();
					states.remove(state);

					// Ability ended, remove ability from using
					using.remove(state);

					// Ability ended, add ability to unusable
					unusable.put(state, cooldownTask);
				}
			}, ability.getDuration());

			// Schedule a task for each AbilityTask
			for (final Ability.AbilityTask task : ability.getAbilityTasks()) {
				timer.scheduleTask(new Timer.Task() {
					@Override
					public void run() {
						task.callback.call();
					}
				}, task.delay);
			}

			// Call ability's begin callback
			ability.begin();
			states.add(state);

			// Ability used, remove ability from usable
			usable.remove(state);

			// Ability used, add ability to using
			using.put(state, endTask);
		}
	}

	/* Update */
	public void update() {
		// Execute "using" abilities callback
		for (Map.Entry<T, Timer.Task> entry : using.entrySet()) {
			Ability ability = abilities.get(entry.getKey());
			ability.using();
		}

		// Check if unusables can be reset
		Iterator<Map.Entry<T, Timer.Task>> iterator = unusable.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<T, Timer.Task> entry = iterator.next();
			T state = entry.getKey();
			Timer.Task task = entry.getValue();
			Ability ability = abilities.get(state);

			// Is on cooldown if cooldown task is still running
			boolean isOnCooldown = task != null;
			if (ability.canReset(isOnCooldown)) {
				if (isOnCooldown) {
					// Cancel task if still running
					task.cancel();
				}

				// Put original ability back in usable
				usable.put(state, ability);

				// Remove ability from unusable
				iterator.remove();
			}
		}
	}

	// TODO: Reset abilities when switching characters
}
