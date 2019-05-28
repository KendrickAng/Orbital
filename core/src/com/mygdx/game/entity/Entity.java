package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Animations;
import com.mygdx.game.GameScreen;
import com.mygdx.game.shape.Rectangle;

/**
 * Represents all renderable, interactive objects such as throwing stars/falling rocks.
 * LivingEntity represents Characters, Bosses.
 */
public abstract class Entity<T extends Enum<T>> {
	public static final int GRAVITY = -2;

	private Vector2 position;
	private Vector2 velocity;
	private float width;
	private float height;

	private boolean visible;
	private boolean dispose;

	private GameScreen game;
	private Sprite sprite;
	private Direction spriteDirection;
	private Rectangle hitbox;

	private T basicState;
	private Animations<T> basicAnimations;

	public Entity(GameScreen game) {
		this.position = new Vector2();
		this.velocity = new Vector2();

		this.visible = true;

		this.game = game;
		this.hitbox = new Rectangle();
		this.basicState = basicState();
		this.basicAnimations = basicAnimations();
		this.spriteDirection = Direction.RIGHT;
		updateHitBox();

		game.getEntityManager().add(this);
	}

	protected abstract T basicState();
	protected abstract Animations<T> basicAnimations();

	protected abstract void update();
	protected abstract void updatePosition(Vector2 position);
	protected abstract void updateVelocity(Vector2 position, Vector2 velocity);

	public void render(SpriteBatch batch) {
		/* Physics */
		// Account for velocity
		updateVelocity(position, velocity);
		position.x += velocity.x;
		position.y += velocity.y;

		// Map bounds check
		updatePosition(position);

		updateHitBox();
		update();

		/* Animations */
		switch (spriteDirection) {
			case RIGHT:
				sprite.setFlip(false, false);
				break;
			case LEFT:
				sprite.setFlip(true, false);
				break;
		}

		/* Rendering */
		sprite.setPosition(position.x, position.y);
		sprite.draw(batch);
	}

	public void renderDebug(ShapeRenderer shapeRenderer) {
		hitbox.renderDebug(shapeRenderer);
	}

	public Sprite updateAnimation() {
		return basicAnimations.from(basicState);
	}

	private void updateHitBox() {
		this.sprite = updateAnimation();
		this.width = sprite.getWidth();
		this.height = sprite.getHeight();

		hitbox.setX(position.x);
		hitbox.setY(position.y);
		hitbox.setWidth(width);
		hitbox.setHeight(height);
	}

	public void dispose() {
		dispose = true;
	}

	/* Setters */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setBasicState(T state) {
		this.basicState = state;
	}

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

	public T getBasicState() {
		return basicState;
	}

	public Animations<T> getBasicAnimations() {
		return basicAnimations;
	}

	public GameScreen getGame() {
		return this.game;
	}
}
