package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.animation.Animations;
import com.mygdx.game.entity.part.Parts;
import com.mygdx.game.entity.state.States;
import com.mygdx.game.shape.Rectangle;

/**
 * Represents all renderable, interactive objects such as throwing stars/falling rocks.
 * LivingEntity represents Characters, Bosses.
 */
public abstract class Entity<S extends Enum, P extends Enum> {
	public static final float GRAVITY = -3;

	private Vector2 position;
	private Vector2 velocity;

	private boolean visible;
	private boolean dispose;

	private GameScreen game;
	private Direction spriteDirection;

	private States<S> states;
	private Parts<P> parts;
	private Animations<S, P> animations;

	public Entity(GameScreen game) {
		this.position = new Vector2();
		this.velocity = new Vector2();

		this.game = game;
		this.visible = true;
		this.spriteDirection = Direction.RIGHT;

		this.states = new States<>();
		this.parts = new Parts<>(position);
		this.animations = new Animations<>(parts);
		this.states.addListener(animations);

		defineAnimations(animations);
		defineStates(states);

		game.getEntityManager().add(this);
	}

	protected abstract void defineStates(States<S> states);

	protected abstract void defineAnimations(Animations<S, P> animations);

	protected abstract void update();

	protected abstract void updatePosition(Vector2 position);

	protected abstract void updateVelocity(Vector2 position, Vector2 velocity);

	public void render(SpriteBatch batch) {
		/* Physics */
		// Account for velocity
		updateVelocity(position, velocity);
		position.x += velocity.x;
		position.y += velocity.y;
		updatePosition(position);

		/* Render */
		switch (spriteDirection) {
			case RIGHT:
				parts.setFlip(false, false);
				break;
			case LEFT:
				parts.setFlip(true, false);
				break;
		}
		parts.render(batch);

		// General updates
		update();
	}

	public void renderDebug(ShapeRenderer shapeRenderer) {
		parts.renderDebug(shapeRenderer);
	}

	public void dispose() {
		dispose = true;
	}

	/* Setters */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void addState(S state) {
		states.addState(state);
	}

	public void removeState(S state) {
		states.removeState(state);
	}

	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
	}

	public void setPosition(Vector2 pos) {
		position.x = pos.x;
		position.y = pos.y;
	}

	public void setVelocity(float x_vel, float y_vel) {
		velocity.x = x_vel;
		velocity.y = y_vel;
	}

	public void setVelocity(Vector2 vel) {
		velocity.x = vel.x;
		velocity.y = vel.y;
	}

	public void setSpriteDirection(Direction spriteDirection) {
		this.spriteDirection = spriteDirection;
	}

	/* Getters */
	public boolean isVisible() {
		return visible;
	}

	public boolean isDispose() {
		return dispose;
	}

	public Rectangle getHitbox(P part) {
		return parts.getHitbox(part);
	}

	public Vector2 getPosition() {
		return position;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public Direction getSpriteDirection() {
		return spriteDirection;
	}

	public Animations<S, P> getAnimations() {
		return animations;
	}

	public GameScreen getGame() {
		return this.game;
	}
}
