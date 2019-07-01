package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen;
import com.mygdx.game.MutableBoolean;
import com.mygdx.game.entity.animation.Animations;
import com.mygdx.game.entity.state.StateListener;
import com.mygdx.game.entity.state.States;

/**
 * Represents all renderable, interactive objects such as throwing stars/falling rocks.
 * LivingEntity represents Characters, Bosses.
 */
public abstract class Entity<I extends Enum, S extends Enum, P extends Enum> {
	public static final float GRAVITY = -3;

	private GameScreen game;
	private EntityData data;

	private States<I, S> states;
	private Animations<S, P> animations;

	private boolean visible;
	private boolean dispose;

	public Entity(GameScreen game) {
		data = new EntityData();

		this.game = game;
		this.visible = true;

		this.states = new States<>();
		this.animations = new Animations<>(data);

		// adds animations, a statelistener, to State's HashSet
		addStateListener(animations);

		defineAnimations(animations);
		defineStates(states);

		game.getEntityManager().add(this);
	}

	protected abstract void defineStates(States<I, S> states);

	protected abstract void defineAnimations(Animations<S, P> animations);

	public void render(SpriteBatch batch) {
		states.update();
		animations.render(batch);
	}

	public void renderDebug(ShapeRenderer shapeRenderer) {
		animations.renderDebug(shapeRenderer);
	}

	public void dispose() {
		dispose = true;
	}

	/* Setters */
	protected void addStateListener(StateListener<S> listener) {
		states.addListener(listener);
	}

	public boolean input(I input) {
		return states.input(input);
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
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
		return data.getPosition();
	}

	public MutableBoolean getFlipX() {
		return data.getFlipX();
	}

	public Color getColor() {
		return data.getColor();
	}

	public GameScreen getGame() {
		return this.game;
	}
}
