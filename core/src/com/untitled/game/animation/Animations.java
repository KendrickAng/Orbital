package com.untitled.game.animation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.untitled.game.EntityData;
import com.untitled.game.Hitbox;
import com.untitled.game.state.StateListener;

import java.util.HashMap;

/**
 * Represents the animation manager for some Entity.
 *
 * @param <S> a State enum.
 * @param <P> a Part enum.
 */
public class Animations<S extends Enum, P extends Enum> implements StateListener<S> {
	// E.g P = AssassinParts. Animation contains a map of all enum parts of AssassinParts to corrs. AnimationPart instance
	private EntityData entityData;
	private Animation<P> animation;

	// Map of states to animation
	private HashMap<S, Animation<P>> animations;

	/**
	 * @param entityData {@link EntityData} of the Entity
	 */
	public Animations(EntityData entityData) {
		this.entityData = entityData;
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
		// Ignore same animation
		if (animation != null && this.animation != animation) {
			if (this.animation != null) {
				this.animation.end();
			}
			this.animation = animation;
			this.animation.setEntityData(entityData);
			this.animation.begin();
		}
	}

	/**
	 * Maps a State enum to an {@link Animation}.
	 *
	 * @param state     the State enum
	 * @param animation the Animation
	 * @return this instance
	 */
	public Animations<S, P> map(S state, Animation<P> animation) {
		animations.put(state, animation);
		return this;
	}

	/**
	 * Render the correct animation based on given State.
	 *
	 * @param batch {@link SpriteBatch} to render the animation on.
	 */
	public void render(SpriteBatch batch) {
		animation.render(batch);
	}

	/**
	 * Render debug of an Animation.
	 *
	 * @param shapeRenderer {@link ShapeRenderer} to render debug on.
	 */
	public void renderDebug(ShapeRenderer shapeRenderer) {
		animation.renderDebug(shapeRenderer);
	}

	/**
	 * Gets the {@link Hitbox} of a Part enum.
	 *
	 * @param part the Part enum.
	 * @return the hitbox corresponding to the Part enum.
	 */
	public Hitbox getHitbox(P part) {
		return animation.getHitbox(part);
	}
}
