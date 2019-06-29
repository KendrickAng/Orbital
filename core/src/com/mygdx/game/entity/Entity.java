package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.animation.Animations;
import com.mygdx.game.entity.state.StateListener;
import com.mygdx.game.entity.state.States;

/**
 * Represents all renderable, interactive objects such as throwing stars/falling rocks.
 * LivingEntity represents Characters, Bosses.
 */
public abstract class Entity<I extends Enum, S extends Enum, P extends Enum> {
	public static final float GRAVITY = -3;

	private Vector2 position;
	private Vector2 velocity;

	private boolean visible;
	private boolean dispose;

	private GameScreen game;

	private States<I, S> states;
	private Animations<S, P> animations;

	public Entity(GameScreen game) {

		this.position = new Vector2(); // position always starts at (0,0)
		this.velocity = new Vector2();

		this.game = game;
		this.visible = true;

		this.states = new States<>();
		this.animations = new Animations<>(position);

		// adds animations, a statelistener, to State's HashSet
		addStateListener(animations);

		defineAnimations(animations);
		defineStates(states);

		game.getEntityManager().add(this);
	}

	protected abstract void defineStates(States<I, S> states);

	protected abstract void defineAnimations(Animations<S, P> animations);

	// General updates
	protected abstract void update();

	protected abstract void updatePosition(Vector2 position);

	public void render(SpriteBatch batch) {
		/* Physics */
		states.updateVelocity(velocity);
		position.x += velocity.x;
		position.y += velocity.y;

		updatePosition(position);
		update();

		/* Render */
		animations.render(batch);
	}

	public void renderDebug(ShapeRenderer shapeRenderer) {
		animations.renderDebug(shapeRenderer);
	}

	public void dispose() {
		dispose = true;
	}

	/* Setters */
	public void input(I input) {
		states.input(input);
	}

	public void addStateListener(StateListener<S> listener) {
		states.addListener(listener);
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
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

	public void setFlipX(boolean flip) {
		animations.setFlipX(flip);
	}

	/* Getters */
	public boolean isVisible() {
		return visible;
	}

	public boolean isDispose() {
		return dispose;
	}

	public Hitbox getHitbox(P part) {
		return animations.getHitbox(part);
	}

	public Vector2 getPosition() {
		return position;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public boolean isFlipX() {
		return animations.getFlipX();
	}

	public GameScreen getGame() {
		return this.game;
	}
}
