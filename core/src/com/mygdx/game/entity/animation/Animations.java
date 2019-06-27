package com.mygdx.game.entity.animation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.Hitbox;
import com.mygdx.game.entity.state.StateListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Represents the animation manager for some Entity.
 */
public class Animations<S extends Enum, P extends Enum> implements StateListener<S> {
	// E.g P = AssassinParts. Animation contains a map of all enum parts of AssassinParts to corrs. AnimationPart instance

	private Vector2 position;// bottom left of the 160 by 80.
	private boolean flipX;
	private boolean flipY;

	private Animation<P> animation; // Dynamic animation (?)

	// States that are currently active
	private HashSet<S> states;

	// Map of states to animation
	private HashMap<HashSet<S>, Animation<P>> animations; // E.g {WALKING,PRIMARY} -> Agroup

	public Animations(Vector2 position) {
		this.position = position;
		this.states = new HashSet<>();
		this.animations = new HashMap<>();
	}

	@Override
	public boolean stateAddValid(S state) {
		return true;
	}

	@Override
	public void stateAdd(S state) {
		states.add(state);
		updateAnimation();
	}

	@Override
	public void stateRemove(S state) {
		states.remove(state);
		updateAnimation();
	}

	// Maps a state to a group. Allows for character to be both standing and walking and using a skill.
	public Animations<S, P> map(Collection<S> states, Animation<P> animation) {
		animations.put(new HashSet<>(states), animation); // E.g {STANDING, WALKING} -> primary
		return this;
	}

	// When a state is added, load active animation group into this instance.
	private void updateAnimation() {
		Animation<P> animation = animations.get(states); // These should be predefined.

		// Ignore undefined animation
		if (animation != null) {
			animation.begin();
			this.animation = animation;
		}
	}

	public void render(SpriteBatch batch) {
		animation.render(batch, position, flipX, flipY);
	}

	public void renderDebug(ShapeRenderer shapeRenderer) {
		animation.renderDebug(shapeRenderer, position, flipX, flipY);
	}

	/* Setters */
	public void setFlip(boolean flipX, boolean flipY) {
		this.flipX = flipX;
		this.flipY = flipY;
	}

	/* Getters */
	public Hitbox getHitbox(P part) {
		return animation.getHitbox(part);
	}

	public Collection<S> getStates() {
		return this.states;
	}
}
