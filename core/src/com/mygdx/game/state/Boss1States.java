package com.mygdx.game.state;

import com.mygdx.game.entity.Boss1;

public class Boss1States {
	public static final StateGroup<Boss1> MOVING = new StateGroup<Boss1>();
	public static final State<Boss1> STANDING = MOVING.add();

	public static final StateGroup<Boss1> ABILITIES = new StateGroup<Boss1>();
	public static final State<Boss1> PRIMARY = ABILITIES.add();
	public static final State<Boss1> SECONDARY = ABILITIES.add();
}
