package com.mygdx.game.entity.debuff;

import com.badlogic.gdx.utils.Timer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/*
An effect on a LivingEntity over a duration of time.
 */
public class Debuffs<T> {
	// only used to schedule new tasks in inflict().
	private Timer timer;
	// Definitions of all debuffs for the LivingEntity. Same across all characters.
	private HashMap<T, Debuff> debuffs;

	// Debuffs that are inflicted on the LivingEntity
	private HashMap<T, HashSet<InflictedDebuff>> inflicted;

	private class InflictedDebuff {
		// added for cleanse. Supposedly task.cancel() everything.
		private Timer.Task task;
		private float modifier;
	}

	public Debuffs() {
		timer = new Timer();
		debuffs = new HashMap<>();
		inflicted = new HashMap<>();
	}

	public Debuffs<T> map(T type, Debuff debuff) {
		debuffs.put(type, debuff);
		inflicted.put(type, new HashSet<>());
		return this;
	}

	// Modifier is from 0f to 1f. (0% - 100%). Main business logic on how functional interfaces DebuffX are called.
	public Debuffs inflict(final T type, final float modifier, float duration) {
		// get the debuff from the map using the enum T.
		final Debuff debuff = debuffs.get(type);
		// allows for different sources of debuffs (E.g by boss and self-inflicted)
		final HashSet<InflictedDebuff> localDebuffs = inflicted.get(type);

		final InflictedDebuff inflictedDebuff = new InflictedDebuff();
		inflictedDebuff.modifier = modifier;
		inflictedDebuff.task = timer.scheduleTask(new Timer.Task() {
			@Override
			public void run() {
				// Debuff ended, untrack InflictedDebuff
				localDebuffs.remove(inflictedDebuff);

				// Debuff ended, recalculate overall modifier before applying.
				debuff.apply(overallModifier(localDebuffs));

				// This is the last debuff of this type, call end
				if (localDebuffs.isEmpty()) {
					debuff.end();
				}
			}
		}, duration);

		// This is the first debuff of this type, call begin
		if (localDebuffs.isEmpty()) {
			debuff.begin();
		}

		// Track InflictedDebuff
		localDebuffs.add(inflictedDebuff);

		// Debuff begin, calculate overall modifier
		debuff.apply(overallModifier(localDebuffs));
		return this;
	}

	// Calculate stacked modifiers
	private float overallModifier(HashSet<InflictedDebuff> debuffs) {
		Iterator<InflictedDebuff> iterator = debuffs.iterator();
		if (iterator.hasNext()) {
			float modifier = iterator.next().modifier;
			while (iterator.hasNext()) {
				modifier *= 1 + iterator.next().modifier;
			}
			return modifier;
		} else {
			return 0;
		}
	}
}
