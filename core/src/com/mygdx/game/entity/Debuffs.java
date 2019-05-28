package com.mygdx.game.entity;

import java.util.HashSet;

/*
An effect on a LivingEntity over a duration of time.
 */
public class Debuffs {
	public enum Debuff {
		SLOW
	}
	private HashSet<Debuff> debuffs;
}
