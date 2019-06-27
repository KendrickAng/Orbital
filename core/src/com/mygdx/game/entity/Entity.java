package com.mygdx.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.entity.animation.Animation;
import com.mygdx.game.entity.animation.Animations;
import com.mygdx.game.entity.state.Boss1States;
import com.mygdx.game.entity.state.CharacterStates;
import com.mygdx.game.entity.state.StateListener;
import com.mygdx.game.entity.state.States;

import java.util.Collection;

import static com.mygdx.game.entity.state.CharacterStates.*;

/**
 * Represents all renderable, interactive objects such as throwing stars/falling rocks.
 * LivingEntity represents Characters, Bosses.
 */
public abstract class Entity<S extends Enum, P extends Enum> {
	private static final String ASSASSINCLASSNAME = "class com.mygdx.game.entity.character.Assassin";
	private static final String TANKCLASSNAME = "class com.mygdx.game.entity.character.Tank";
	private static final String BOSSCLASSNAME = "class com.mygdx.game.entity.boss1.Boss1";

	public static final float GRAVITY = -3;

	private Vector2 position;
	private Vector2 velocity;

	private boolean visible;
	private boolean dispose;

	private GameScreen game;
	private Direction spriteDirection;

	private States<S> states;
	private Animations<S, P> animations;

	public Entity(GameScreen game) {

		this.position = new Vector2(); // position always starts at (0,0)
		this.velocity = new Vector2();

		this.game = game;
		this.visible = true;
		this.spriteDirection = Direction.RIGHT;

		this.states = new States<>();
		this.animations = new Animations<>(position);

		// adds animations, a statelistener, to State's HashSet
		addStateListener(animations);
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
		updateVelocity(position, velocity);
		position.x += velocity.x;
		position.y += velocity.y;
		updatePosition(position);

		/* Render */
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

	public void renderDebug(ShapeRenderer shapeRenderer) {
		animations.renderDebug(shapeRenderer);
	}

	public void dispose() {
		dispose = true;
	}

	/* Setters */
	public void addStateListener(StateListener<S> listener) {
		states.addListener(listener);
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void addState(S state) {
		states.addState(state);
	}

	public void removeState(S state) {
		states.removeState(state);
	}

	public void scheduleState(S state, float duration) {
		states.scheduleState(state, duration);
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

	public Hitbox getHitbox(P part) {
		return animations.getHitbox(part);
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

	public GameScreen getGame() {
		return this.game;
	}

	public Collection<S> getStates() { return this.animations.getStates(); }

	// This function is dependent on class and package names.
	public boolean getIsIdle() {
		switch(this.getClass().toString()) {
			case ASSASSINCLASSNAME:
			case TANKCLASSNAME:
				Collection cStates = this.getStates();
				return !(cStates.contains(CharacterStates.PRIMARY) ||
						cStates.contains(CharacterStates.SECONDARY) ||
						cStates.contains(CharacterStates.TERTIARY));
			case BOSSCLASSNAME:
				Collection bStates = this.getStates();
				return !(bStates.contains(Boss1States.PRIMARY) ||
						bStates.contains(Boss1States.SECONDARY) ||
						bStates.contains(Boss1States.TERTIARY));
			default:
				return false;
		}
	}
}
