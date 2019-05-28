package com.mygdx.game.state;

import com.mygdx.game.entity.Boss1;
import com.mygdx.game.entity.Character;
import com.mygdx.game.entity.Shuriken;

public class CharacterStates {
	public static final StateGroup<Character> MOVING = new StateGroup<Character>();
	public static final State<Character> STANDING = MOVING.add();
	public static final State<Character> WALKING = MOVING.add();

	public static final StateGroup<Character> ABILITIES = new StateGroup<Character>();
	public static final State<Character> PRIMARY = ABILITIES.add();
	public static final State<Character> SECONDARY = ABILITIES.add();

	// Ultimates should be able to be used together with ordinary skills
	public static final StateGroup<Character> ABILITIES_ULTIMATE = new StateGroup<Character>();
	public static final State<Character> TERTIARY = ABILITIES_ULTIMATE.add();
}
