package com.mygdx.game.entity.ability;

import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.entity.state.StateListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * What abilities an entity can use. What S can be is defined in Character's defineAbilities().
 */
public class Abilities<S extends Enum> implements StateListener<S> {
	private Timer timer;

	// Abilities defined. Library.
	private HashMap<S, Ability> abilities;

	// Abilities that can be used
	private HashMap<S, Ability> usable;

	// Abilities that are currently active
	private HashMap<S, Ability> using;

	// Abilities that cannot be used and need to be checked if they can be reset
	private HashMap<S, Timer.Task> unusable;

	public Abilities() {
		this.timer = new Timer();

		abilities = new HashMap<>();
		usable = new HashMap<>();
		using = new HashMap<>();
		unusable = new HashMap<>();
	}

	// Map a state to an ability
	public Abilities<S> map(S state, Ability ability) {
		abilities.put(state, ability); // never changes once defined.
		usable.put(state, ability); // an ability is removed from HM and put in using when the ability is used, until cooldown is up.
		return this;
	}

	/* Update is called every iteration of the game loop. */
	public void update() {
		// Execute "using" abilities callback
		for (Map.Entry<S, Ability> entry : using.entrySet()) {
			entry.getValue().using();
		}

		// Check if unusables can be reset
		Iterator<Map.Entry<S, Timer.Task>> iterator = unusable.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<S, Timer.Task> entry = iterator.next();
			S state = entry.getKey();
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

	// valid add if the key-pair is in usable AND no other abilities being used (0).
	@Override
	public boolean stateAddValid(S state) {
		if (abilities.containsKey(state)) {
			Ability ability = usable.get(state);
			return ability != null && ability.canUse(using.size());
		}
		return true;
	}

	// Detects there's an ability being used in States, and activates the logic.
	@Override
	public void stateAdd(S state) {
		if (abilities.containsKey(state)) {
			Ability ability = usable.get(state);

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

			// Ability used, remove ability from usable
			usable.remove(state);

			// Ability used, add ability to using
			using.put(state, ability);
		}
	}

	@Override
	public void stateRemove(S state) {
		if (abilities.containsKey(state)) {
			Ability ability = using.get(state);

			if (ability != null) {
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

				// Ability ended, remove ability from using
				using.remove(state);

				// Ability ended, add ability to unusable
				unusable.put(state, cooldownTask);
			}
		}
	}

	// TODO: Reset abilities when switching characters
}
