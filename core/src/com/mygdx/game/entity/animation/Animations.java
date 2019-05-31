package com.mygdx.game.entity.animation;

import com.mygdx.game.entity.part.Parts;
import com.mygdx.game.entity.state.StateListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Eventually change from sprites to animations.
 */
public class Animations<S extends Enum, P extends Enum> implements StateListener<S> {
	private Parts<P> parts;

	// States that are currently active
	private HashSet<S> states;

	// Map of states to animation
	private HashMap<HashSet<S>, AnimationsGroup<P>> groups;

	public Animations(Parts<P> parts) {
		this.parts = parts;
		this.states = new HashSet<>();
		this.groups = new HashMap<>();
	}

	@Override
	public boolean stateAddValid(S state) {
		return true;
	}

	@Override
	public void stateAdd(S state) {
		states.add(state);
		updateParts();
	}

	@Override
	public void stateRemove(S state) {
		states.remove(state);
		updateParts();
	}

	// Maps a state to a group
	public Animations<S, P> map(Collection<S> states, AnimationsGroup<P> group) {
		groups.put(new HashSet<>(states), group);
		return this;
	}

	private void updateParts() {
		AnimationsGroup<P> group = groups.get(states);
		if (group != null) {
			parts.setAnimationsGroup(group);
		}
	}
}
