package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MutableBoolean;

public class EntityData {
	private Vector2 position;
	private MutableBoolean flipX;
	private Color color;

	public EntityData() {
		this.position = new Vector2();
		this.flipX = new MutableBoolean();
		this.color = new Color(1, 1, 1, 1);
	}

	public Vector2 getPosition() {
		return position;
	}

	public MutableBoolean getFlipX() {
		return flipX;
	}

	public Color getColor() {
		return color;
	}
}
