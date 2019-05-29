package com.mygdx.game.entity.debuff;

import com.badlogic.gdx.utils.Timer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/*
An effect on a LivingEntity over a duration of time.
 */
public class Debuffs<T> {
	private Timer timer;
	// Definitions of all debuffs for the LivingEntity
	private HashMap<T, Debuff> debuffs;

	// Debuffs that are inflicted on the LivingEntity
	private HashMap<T, HashSet<InflictedDebuff>> inflicted;

	private class InflictedDebuff {
		private Timer.Task task;
		private float modifier;
	}

	public Debuffs() {
		timer = new Timer();
		debuffs = new HashMap<T, Debuff>();
		inflicted = new HashMap<T, HashSet<InflictedDebuff>>();
	}

	public Debuffs<T> define(T type, Debuff debuff) {
		debuffs.put(type, debuff);
		inflicted.put(type, new HashSet<InflictedDebuff>());
		return this;
	}

	// Modifier is from 0f to 1f. (0% - 100%)
	public Debuffs inflict(final T type, final float modifier, float duration) {
		final Debuff debuff = debuffs.get(type);
		final HashSet<InflictedDebuff> debuffs = inflicted.get(type);

		final InflictedDebuff inflictedDebuff = new InflictedDebuff();
		inflictedDebuff.modifier = modifier;
		inflictedDebuff.task = timer.scheduleTask(new Timer.Task() {
			@Override
			public void run() {
				// Debuff ended, untrack InflictedDebuff
				debuffs.remove(inflictedDebuff);

				// Debuff ended, recalculate overall modifier
				debuff.apply(overallModifier(debuffs));

				// This is the last debuff of this type, call end
				if (debuffs.isEmpty()) {
					debuff.end();
				}
			}
		}, duration);

		// This is the first debuff of this type, call begin
		if (debuffs.isEmpty()) {
			debuff.begin();
		}

		// Track InflictedDebuff
		debuffs.add(inflictedDebuff);

		// Debuff begin, calculate overall modifier
		debuff.apply(overallModifier(debuffs));
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
