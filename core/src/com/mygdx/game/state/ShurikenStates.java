package com.mygdx.game.state;

import com.mygdx.game.entity.Shuriken;

public class ShurikenStates {
	public static final StateGroup<Shuriken> MOVING = new StateGroup<Shuriken>();
	public static final State<Shuriken> FLYING = MOVING.add();
}
