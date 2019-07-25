package com.mygdx.game.screens.game.ability;

import com.mygdx.game.screens.game.state.StateListener;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Abilities manager.
 */
public class Abilities<S extends Enum> implements StateListener<S> {
	// Abilities to use in state.
	private HashMap<S, Ability<S>> abilitiesBegin;

	// Abilities to cancel in state.
	private HashMap<S, HashSet<Ability<S>>> abilitiesEnd;

	public Abilities() {
		abilitiesBegin = new HashMap<>();
		abilitiesEnd = new HashMap<>();
	}

	// Map a state to an ability to use. (1 to 1)
	public Abilities<S> addBegin(S state, Ability<S> ability) {
		abilitiesBegin.put(state, ability);
		return this;
	}

	// Add an ability to be cancelled when in a state.
	public Abilities<S> addEnd(S state, Ability<S> ability) {
		HashSet<Ability<S>> abilities = this.abilitiesEnd.get(state);
		if (abilities == null) {
			abilities = new HashSet<>();
			this.abilitiesEnd.put(state, abilities);
		}
		abilities.add(ability);
		return this;
	}


	@Override
	public boolean stateValid(S state) {
		if (abilitiesBegin.containsKey(state)) {
			Ability<S> ability = abilitiesBegin.get(state);
			return ability.isReady();
		}
		return true;
	}

	@Override
	public void stateChange(S state) {
		if (abilitiesBegin.containsKey(state)) {
			Ability<S> ability = abilitiesBegin.get(state);
			ability.begin(state);
		}

		if (abilitiesEnd.containsKey(state)) {
			for (Ability<S> ability : abilitiesEnd.get(state)) {
				ability.end();
			}
		}
	}
}
