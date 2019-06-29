package com.mygdx.game.entity;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MutableBoolean;

public class EntityData {
	private Vector2 position;
	private MutableBoolean flipX;

	public EntityData() {
		this.position = new Vector2();
		this.flipX = new MutableBoolean();
	}

	public Vector2 getPosition() {
		return position;
	}

	public MutableBoolean getFlipX() {
		return flipX;
	}
}
