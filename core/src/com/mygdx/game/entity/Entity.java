package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Animations;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.state.States;

/**
 * Represents all renderable, interactive objects such as throwing stars/falling rocks.
 * LivingEntity represents Characters, Bosses.
 */
public abstract class Entity<T> {
	public static final int GRAVITY = -2;

	private Vector2 position;
	private Vector2 velocity;
	private float width;
	private float height;

	private MyGdxGame game;
	private Sprite sprite;
	private Direction spriteDirection;

	private States<T> states;
	private Animations<T> animations;

	public Entity(MyGdxGame game) {
		this.position = new Vector2();
		this.velocity = new Vector2();

		this.game = game;
		this.states = states();
		this.animations = animations();
		this.spriteDirection = Direction.RIGHT;
		updateAnimations();
	}

	protected abstract States<T> states();

	protected abstract Animations<T> animations();

	protected abstract void updatePosition(Vector2 position);

	protected abstract void updateVelocity(Vector2 position, Vector2 velocity);

	public void render(SpriteBatch batch) {
		/* Animations */
		updateAnimations();
		switch (spriteDirection) {
			case RIGHT:
				sprite.setFlip(false, false);
				break;
			case LEFT:
				sprite.setFlip(true, false);
				break;
		}

		// Account for velocity
		updateVelocity(position, velocity);
		position.x += velocity.x;
		position.y += velocity.y;

		// Map bounds check
		updatePosition(position);

		/* Rendering */
		sprite.setPosition(position.x, position.y);
		sprite.draw(batch);
	}

	private void updateAnimations() {
		this.sprite = animations.from(states);
		this.width = sprite.getWidth();
		this.height = sprite.getHeight();
	}

	// TODO: Dispose

	/* Setters */
	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
	}

	public void setPosition(Vector2 pos) {
		position.x = pos.x;
		position.y = pos.y;
	}

	public void setVelocity(int x_vel, int y_vel) {
		velocity.x = x_vel;
		velocity.y = y_vel;
	}

	public void setSpriteDirection(Direction spriteDirection) {
		this.spriteDirection = spriteDirection;
	}

	/* Getters */
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

	public Vector2 getPosition() {
		return position;
	}

	public Direction getSpriteDirection() {
		return spriteDirection;
	}

	public States<T> getStates() {
		return states;
	}

	public MyGdxGame getGame() {
		return this.game;
	}
}
