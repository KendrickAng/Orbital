package com.mygdx.game.screens.game.animation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.screens.game.EntityData;
import com.mygdx.game.screens.game.Hitbox;
import com.mygdx.game.screens.game.state.StateListener;

import java.util.HashMap;

/**
 * Represents the animation manager for some Entity.
 */
public class Animations<S extends Enum, P extends Enum> implements StateListener<S> {
	// E.g P = AssassinParts. Animation contains a map of all enum parts of AssassinParts to corrs. AnimationPart instance
	private EntityData entityData;
	private Animation<P> animation;

	// Map of states to animation
	private HashMap<S, Animation<P>> animations;

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

	// Maps a state to an animation.
	public Animations<S, P> map(S state, Animation<P> animation) {
		animations.put(state, animation);
		return this;
	}

	public void render(SpriteBatch batch) {
		animation.render(batch);
	}

	public void renderDebug(ShapeRenderer shapeRenderer) {
		animation.renderDebug(shapeRenderer);
	}

	public Hitbox getHitbox(P part) {
		return animation.getHitbox(part);
	}
}
