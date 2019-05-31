package com.mygdx.game.entity.animation;

import com.mygdx.game.entity.part.Parts;
import com.mygdx.game.entity.state.StateListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;

/**
 * Eventually change from sprites to animations.
 */
public class Animations<S extends Enum, P extends Enum> implements StateListener<S> {
	private Parts<P> parts;
	private HashMap<S, AnimationsGroup<P>> groups;

	// Groups that are currently active based on states
	private HashSet<AnimationsGroup<P>> activeGroups;

	public Animations(Parts<P> parts) {
		this.parts = parts;
		this.groups = new HashMap<>();
		this.activeGroups = new HashSet<>();
	}

	@Override
	public boolean stateAddValid(S state) {
		return true;
	}

	@Override
	public void stateAdd(S state) {
		AnimationsGroup<P> group = groups.get(state);
		if (group != null) {
			activeGroups.add(group);
			for (Map.Entry<P, Animation> entry : animations().entrySet()) {
				parts.put(entry.getKey(), entry.getValue());
			}
		}
	}

	@Override
	public void stateRemove(S state) {
		AnimationsGroup<P> group = groups.get(state);
		if (group != null) {
			activeGroups.remove(group);
			for (Map.Entry<P, Animation> entry : animations().entrySet()) {
				parts.put(entry.getKey(), entry.getValue());
			}
		}
	}

	// Maps a state to a group
	public Animations<S, P> map(S state, AnimationsGroup<P> group) {
		groups.put(state, group);
		return this;
	}

	// Finds the best animations based on priorities.
	private Map<P, Animation> animations() {
		HashMap<P, Animation> partAnimations = new HashMap<>();
		for (AnimationsGroup<P> group : new TreeSet<>(activeGroups)) {
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
