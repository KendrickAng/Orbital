package com.mygdx.game.entity.ability;

import com.mygdx.game.entity.state.StateListener;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Abilities manager.
 */
public class Abilities<S extends Enum> implements StateListener<S> {
	// Abilities to use in state.
	private HashMap<S, Ability> abilitiesUse;

	// Abilities to cancel in state.
	private HashMap<S, HashSet<Ability>> abilitiesCancel;

	public Abilities() {
		abilitiesUse = new HashMap<>();
		abilitiesCancel = new HashMap<>();
	}

	// Map a state to an ability to use. (1 to 1)
	public Abilities<S> defineUse(S state, Ability ability) {
		abilitiesUse.put(state, ability);
		return this;
	}

	// Add an ability to be cancelled when in a state.
	public Abilities<S> addCancel(S state, Ability ability) {
		HashSet<Ability> abilitiesCancel = this.abilitiesCancel.get(state);
		if (abilitiesCancel == null) {
			abilitiesCancel = new HashSet<>();
			this.abilitiesCancel.put(state, abilitiesCancel);
		}
		abilitiesCancel.add(ability);
		return this;
	}


	@Override
	public boolean stateValid(S state) {
		if (abilitiesUse.containsKey(state)) {
			Ability ability = abilitiesUse.get(state);
			return ability.isReady();
		}
		return true;
	}

	@Override
	public void stateChange(S state) {
		if (abilitiesUse.containsKey(state)) {
			Ability ability = abilitiesUse.get(state);
			ability.begin();
		}

		if (abilitiesCancel.containsKey(state)) {
			for (Ability ability : abilitiesCancel.get(state)) {
				ability.end();
			}
		}
	}

	// TODO: Reset abilities when switching characters
}
