package com.mygdx.game.entity.animation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.Hitbox;
import com.mygdx.game.entity.state.StateListener;

import java.util.HashMap;

/**
 * Represents the animation manager for some Entity.
 */
public class Animations<S extends Enum, P extends Enum> implements StateListener<S> {
	// E.g P = AssassinParts. Animation contains a map of all enum parts of AssassinParts to corrs. AnimationPart instance

	private Vector2 position;// bottom left of the 160 by 80.
	private boolean flipX;

	private Animation<P> animation;

	// Map of states to animation
	private HashMap<S, Animation<P>> animations;

	public Animations(Vector2 position) {
		this.position = position;
		this.animations = new HashMap<>();
	}

	@Override
	public boolean stateValid(S state) {
		return true;
	}

	@Override
	public void stateChange(S state) {
		Animation<P> animation = animations.get(state);

		// Ignore null animations
		if (animation != null) {
			this.animation = animation;
			this.animation.begin();
		}
	}

	// Maps a state to a group.
	public Animations<S, P> map(S state, Animation<P> animation) {
		animations.put(state, animation);
		return this;
	}

	public void render(SpriteBatch batch) {
		animation.render(batch, position, flipX);
	}

	public void renderDebug(ShapeRenderer shapeRenderer) {
		animation.renderDebug(shapeRenderer, position, flipX);
	}

	/* Setters */
	public void setFlipX(boolean flip) {
		flipX = flip;
	}

	/* Getters */
	public boolean getFlipX() {
		return flipX;
	}

	public Hitbox getHitbox(P part) {
		return animation.getHitbox(part);
	}
}
