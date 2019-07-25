package com.mygdx.game.screens.game.ability;

import com.mygdx.game.screens.game.state.StateListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Abilities manager. Defines when an ability begins or ends based on state by mapping state to
 * ability calls.
 * @param <S> a State enum.
 */
public class Abilities<S extends Enum> implements StateListener<S> {
	private LinkedList<Ability<S>> abilities;

	// Abilities to use in state.
	private HashMap<S, Ability<S>> abilitiesUse;

	// Abilities to cancel in state.
	private HashMap<S, HashSet<Ability<S>>> abilitiesCancel;

	public Abilities() {
		abilities = new LinkedList<>();
		abilitiesUse = new HashMap<>();
		abilitiesCancel = new HashMap<>();
	}

	/**
	 * Maps a state to an ability begin.
	 * @param state the new state being added.
	 * @param ability the corresponding ability to the state.
	 * @return the current Abilities instance.
	 */
	public Abilities<S> addBegin(S state, Ability<S> ability) {
		abilitiesUse.put(state, ability);
		if (!abilities.contains(ability)) {
			abilities.add(ability);
		}
		return this;
	}

	// Add an ability to be cancelled when in a state.

	/**
	 * Maps a state to an ability end. Here, one state can end multiple abilities.
	 * @param state the new state being added.
	 * @param ability the ability to end.
	 * @return the current Abilities instance.
	 */
	public Abilities<S> addEnd(S state, Ability<S> ability) {
		HashSet<Ability<S>> abilitiesCancel = this.abilitiesCancel.get(state);
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
			Ability<S> ability = abilitiesUse.get(state);
			return ability.isReady();
		}
		return true;
	}

	@Override
	public void stateChange(S state) {
		if (abilitiesUse.containsKey(state)) {
			Ability<S> ability = abilitiesUse.get(state);
			ability.begin(state);
		}

		if (abilitiesCancel.containsKey(state)) {
			for (Ability<S> ability : abilitiesCancel.get(state)) {
				ability.end();
			}
		}
	}

	public Ability[] getAbilities() {
		return abilities.toArray(new Ability[0]);
	}
}
