package com.untitled.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.untitled.MutableBoolean;
import com.untitled.MutableFloat;

/**
 * Position, flip, alpha and color of an Entity.
 * (Mainly used in rendering)
 */
public class EntityData {
	private Vector2 position;
	private MutableBoolean flipX;
	private MutableFloat alpha;
	private Color color;

	public EntityData() {
		this.position = new Vector2();
		this.flipX = new MutableBoolean(false);
		this.alpha = new MutableFloat(1f);
		this.color = new Color(1, 1, 1, 1);
	}

	public Vector2 getPosition() {
		return position;
	}

	public MutableBoolean getFlipX() {
		return flipX;
	}

	public MutableFloat getAlpha() {
		return alpha;
	}

	public Color getColor() {
		return color;
	}
}
