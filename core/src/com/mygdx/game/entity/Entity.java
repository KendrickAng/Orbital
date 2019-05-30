package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.animation.Animations;
import com.mygdx.game.shape.Rectangle;

import java.util.HashSet;

/**
 * Represents all renderable, interactive objects such as throwing stars/falling rocks.
 * LivingEntity represents Characters, Bosses.
 */
public abstract class Entity<T extends Enum, R extends Enum> {
	public static final float GRAVITY = -3;

	private Vector2 position;
	private Vector2 velocity;
	private float width;
	private float height;

	private boolean visible;
	private boolean dispose;

	private GameScreen game;
	private Direction spriteDirection;
	private Rectangle hitbox;

	private HashSet<T> states;
	private Animations<T, R> animations;

	public Entity(GameScreen game) {
		this.position = new Vector2();
		this.velocity = new Vector2();

		this.visible = true;
		this.spriteDirection = Direction.RIGHT;

		this.game = game;
		this.states = new HashSet<>();
		states(states);
		this.animations = animations();
		updateHitbox();

		game.getEntityManager().add(this);
	}

	protected abstract void states(HashSet<T> states);

	protected abstract Animations<T, R> animations();

	protected abstract void update();

	protected abstract void updatePosition(Vector2 position);

	protected abstract void updateVelocity(Vector2 position, Vector2 velocity);

	protected abstract Rectangle hitbox();

	public void render(SpriteBatch batch) {
		/* Physics */
		// Account for velocity
		updateVelocity(position, velocity);
		position.x += velocity.x;
		position.y += velocity.y;
		updatePosition(position);

		/* Animations */
		updateHitbox();
		animations.setPosition(position);
		switch (spriteDirection) {
			case RIGHT:
				animations.setFlip(false, false);
				break;
			case LEFT:
				animations.setFlip(true, false);
				break;
		}
		animations.render(batch);

		// General updates
		update();
	}

	private void updateHitbox() {
		hitbox = hitbox();
		width = hitbox.getWidth();
		height = hitbox.getHeight();
	}

	public void renderDebug(ShapeRenderer shapeRenderer) {
		animations.renderDebug(shapeRenderer);
	}

	public void dispose() {
		dispose = true;
	}

	/* Setters */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void addState(T state) {
		states.add(state);
	}

	public void removeState(T state) {
		states.remove(state);
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

	public float getX() {
		return position.x;
	}

	public float getY() {
		return position.y;
	}

	public float getWidth() {
		return this.width;
	}

	public float getHeight() {
		return this.height;
	}

	public Rectangle getHitbox() {
		return hitbox;
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

	public HashSet<T> getStates() {
		return states;
	}

	public Animations<T, R> getAnimations() {
		return animations;
	}

	public GameScreen getGame() {
		return this.game;
	}
}
