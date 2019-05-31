package com.mygdx.game.entity.part;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.Hitbox;
import com.mygdx.game.entity.animation.Animation;

import java.util.TreeMap;

public class Parts<P extends Enum> {
	private Vector2 position;
	private boolean flipX;
	private boolean flipY;

	private TreeMap<P, Animation> parts;

	public Parts(Vector2 position) {
		this.position = position;
		this.parts = new TreeMap<>();
	}

	public void put(P part, Animation animation) {
		animation.setPosition(position);
		parts.put(part, animation);
	}

	public void render(SpriteBatch batch) {
		for (Animation animation : parts.values()) {
			animation.setFlip(flipX, flipY);
			animation.render(batch);
		}
	}

	public void renderDebug(ShapeRenderer shapeRenderer) {
		for (Animation animation : parts.values()) {
			animation.getHitbox().renderDebug(shapeRenderer);
		}
	}

	/* Setters */
	public void setFlip(boolean flipX, boolean flipY) {
		this.flipX = flipX;
		this.flipY = flipY;
	}

	public Hitbox getHitbox(P part) {
		return parts.get(part).getHitbox();
	}
}
