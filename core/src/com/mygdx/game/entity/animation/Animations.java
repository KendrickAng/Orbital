package com.mygdx.game.entity.animation;

import com.mygdx.game.entity.part.Parts;
import com.mygdx.game.entity.state.StateListener;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Eventually change from sprites to animations.
 */
public class Animations<S extends Enum, P extends Enum> implements StateListener<S> {
	private Parts<P> parts;
	private HashMap<S, AnimationsGroup<P>> groups;

	// Groups that are currently active based on states
	private TreeSet<AnimationsGroup<P>> activeGroups;

	public Animations(Parts<P> parts) {
		this.parts = parts;
		this.groups = new HashMap<>();
		this.activeGroups = new TreeSet<>();
	}

	@Override
	public void stateAdd(S state) {
		activeGroups.add(groups.get(state));
		for (Map.Entry<P, Animation> entry : animations().entrySet()) {
			parts.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void stateRemove(S state) {
		activeGroups.remove(groups.get(state));
	}

	// Maps a state to a group
	public Animations<S, P> map(S state, AnimationsGroup<P> group) {
		groups.put(state, group);
		return this;
	}

	// Finds the best animations based on priorities.
	private Map<P, Animation> animations() {
		HashMap<P, Animation> partAnimations = new HashMap<>();
		for (AnimationsGroup<P> group : activeGroups) {
			for (Map.Entry<P, Animation> entry : group.getAnimations().entrySet()) {
				P part = entry.getKey();
				Animation animation = entry.getValue();
				if (!partAnimations.containsKey(part)) {
					partAnimations.put(part, animation);
				}
			}
		}
		return partAnimations;
	}
}
