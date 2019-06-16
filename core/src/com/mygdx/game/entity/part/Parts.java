package com.mygdx.game.entity.part;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.Hitbox;
import com.mygdx.game.entity.animation.Animation;
import com.mygdx.game.entity.animation.AnimationsGroup;

import java.util.HashMap;
import java.util.TreeMap;

// TODO: Integrate parts with animations group and remove parts.
// for getting hitboxes in O(1) and rendering the player.
public class Parts<P extends Enum> {
	private Vector2 position;// bottom left of the 160 by 80.
	private boolean flipX;
	private boolean flipY;

	private TreeMap<P, Animation> animations;
	private HashMap<P, Animation> parts;

	public Parts(Vector2 position) {
		this.position = position;
	}

	// locks in a new animation group to render() in Entity.
	public void setAnimationsGroup(AnimationsGroup<P> group) {
		group.setPosition(position);
		this.parts = group.getParts();
		this.animations = group.getAnimations();
	}

	public void render(SpriteBatch batch) {
		for (Animation animation : animations.values()) {
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
