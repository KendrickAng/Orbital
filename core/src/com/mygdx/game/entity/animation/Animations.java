package com.mygdx.game.entity.animation;

import com.mygdx.game.entity.state.StateListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Represents the animation manager for some Entity. Eventually change from sprites to animations.
 */
public class Animations<S extends Enum, P extends Enum> implements StateListener<S> {
	/* -----------------MIGRATED FROM PARTS----------------- */
	// E.g P = AssassinParts. AnimationsGroup contains a map of all enum parts of AssassinParts to corrs. Animation instance
	// Contains the animationgroup corresponding to current active state.
	private AnimationsGroup<P> animationStore;
	/* -----------------MIGRATED FROM PARTS----------------- */

	// States that are currently active
	private HashSet<S> states;

	// Map of states to animation
	private HashMap<HashSet<S>, AnimationsGroup<P>> groups; // E.g {WALKING,PRIMARY} -> Agroup

	/* -----------------MIGRATED FROM PARTS----------------- */
	public Animations(AnimationsGroup<P> animationStore) {
		this.animationStore = animationStore;
		this.states = new HashSet<>();
		this.groups = new HashMap<>();
	}
	/* -----------------MIGRATED FROM PARTS----------------- */

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

	// Maps a state to a group. Allows for character to be both standing and walking and using a skill.
	public Animations<S, P> map(Collection<S> states, AnimationsGroup<P> group) {
		groups.put(new HashSet<>(states), group); // E.g {STANDING, WALKING} -> primary
		return this;
	}

	// When a state is added, load active animation group into this instance.
	private void updateParts() {
		AnimationsGroup<P> group = groups.get(states); // These should be predefined.
		if (group != null) {
			/* -----------------MIGRATED FROM PARTS----------------- */
			animationStore.setAnimationsGroup(group);
			/* -----------------MIGRATED FROM PARTS----------------- */
		}
	}
}
