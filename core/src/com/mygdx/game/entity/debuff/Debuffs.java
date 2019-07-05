package com.mygdx.game.entity.debuff;

import com.badlogic.gdx.utils.Timer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/*
An effect on a LivingEntity over a duration of time.
 */
public class Debuffs {
	// only used to schedule new tasks in inflict().
	private Timer timer;
	// Definitions of all debuffs for the LivingEntity.
	private HashMap<DebuffType, DebuffDefinition> definitions;

	// Debuffs that are inflicted on the LivingEntity
	private HashMap<DebuffType, HashSet<Debuff>> inflicted;

	public Debuffs() {
		timer = new Timer();
		definitions = new HashMap<>();
		inflicted = new HashMap<>();
	}

	public Debuffs map(DebuffType type, DebuffDefinition debuff) {
		definitions.put(type, debuff);
		inflicted.put(type, new HashSet<>());
		return this;
	}

	// Modifier is from 0f to 1f. (0% - 100%). Main business logic on how functional interfaces DebuffX are called.
	public void inflict(Debuff debuff) {
		// get the debuff from the map using the enum T.
		DebuffDefinition definition = definitions.get(debuff.getType());
		HashSet<Debuff> debuffs = inflicted.get(debuff.getType());

		// Individual debuff begin.
		debuff.begin();

		// Cancel debuff after debuff duration
		// Debuffs with 0 duration will be infinitely long.
		if (debuff.getDuration() > 0) {
			timer.scheduleTask(new Timer.Task() {
				@Override
				public void run() {
					Debuffs.this.cancel(debuff);
				}
			}, debuff.getDuration());
		}

		// This is the first debuff of this type, call begin
		if (debuffs.isEmpty()) {
			definition.begin();
		}

		// Track InflictedDebuff
		debuffs.add(debuff);

		// Debuff begin, calculate overall modifier
		definition.update(updateModifier(debuffs));
	}

	public void cancel(DebuffType type) {
		HashSet<Debuff> debuffs = inflicted.get(type);
		for (Debuff debuff : debuffs) {
			cancel(debuff);
		}
	}

	public void cancel(Debuff debuff) {
		DebuffDefinition definition = definitions.get(debuff.getType());
		HashSet<Debuff> debuffs = inflicted.get(debuff.getType());

		// Individual debuff end.
		debuff.end();

		// Debuff ended, untrack InflictedDebuff
		debuffs.remove(debuff);

		// Debuff ended, recalculate overall modifier before applying.
		definition.update(updateModifier(debuffs));

		// This is the last debuff of this type, call end
		if (debuffs.isEmpty()) {
			definition.end();
		}
	}

	// Calculate stacked modifiers
	private float updateModifier(HashSet<Debuff> debuffs) {
		Iterator<Debuff> iterator = debuffs.iterator();
		if (iterator.hasNext()) {
			float modifier = iterator.next().getModifier();
			while (iterator.hasNext()) {
				modifier *= 1 + iterator.next().getModifier();
			}
			return modifier;
		} else {
			return 0;
		}
	}
}
