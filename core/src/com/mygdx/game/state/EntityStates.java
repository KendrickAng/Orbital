package com.mygdx.game.state;

import com.mygdx.game.Character;
import com.mygdx.game.Entity;

public class EntityStates {
	public static final StateGroup<Character> MOVING = new StateGroup<Character>();
	public static final State<Character> STANDING = MOVING.add();
	public static final State<Character> WALKING = MOVING.add();

	public static final StateGroup<Character> ABILITIES = new StateGroup<Character>();
	public static final State<Character> PRIMARY = ABILITIES.add();
	public static final State<Character> SECONDARY = ABILITIES.add();
	public static final State<Character> TERTIARY = ABILITIES.add();

	public static final StateGroup<Entity> SHURIKEN = new StateGroup<Entity>();
	public static final State<Entity> FLYING = SHURIKEN.add();
}