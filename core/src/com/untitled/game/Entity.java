package com.untitled.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.untitled.MutableBoolean;
import com.untitled.MutableFloat;
import com.untitled.assets.Assets;
import com.untitled.game.animation.Animations;
import com.untitled.game.state.StateListener;
import com.untitled.game.state.States;
import com.untitled.screens.GameScreen;

/**
 * Represents objects with:
 * - {@link States}
 * - {@link Animations}
 * - Parts
 * - Input
 */
public abstract class Entity<I extends Enum, S extends Enum, P extends Enum> {
	public static final float GRAVITY = -3f;

	private GameScreen game;
	private EntityData data;
	private RenderTask renderTask;

	private States<I, S> states;
	private Animations<S, P> animations;

	private boolean dispose;

	/**
	 * @param game           the GameScreen this Entity should use.
	 * @param renderPriority priority to render in EntityManager.
	 */
	public Entity(GameScreen game, int renderPriority) {
		data = new EntityData();

		this.game = game;
		this.states = new States<>();
		this.animations = new Animations<>(data);

		// adds animations, a statelistener, to State's HashSet
		addStateListener(animations);

		defineAnimations(animations, game.getAssets());
		defineStates(states);

		game.getEntityManager().add(this, renderPriority);
	}

	/**
	 * @param states define states in this States instance.
	 */
	protected abstract void defineStates(States<I, S> states);

	/**
	 * @param animations define animations in this Animations instance.
	 * @param assets     Untitled {@link Assets}.
	 */
	protected abstract void defineAnimations(Animations<S, P> animations, Assets assets);

	/**
	 * Renders this Entity.
	 *
	 * @param batch {@link SpriteBatch} to render this Entity on.
	 */
	public void render(SpriteBatch batch) {
		states.update();
		animations.render(batch);

		if (renderTask != null) {
			renderTask.render(batch);
		}
	}

	/**
	 * Renders the debug of animations.
	 *
	 * @param shapeRenderer {@link ShapeRenderer} to render debug on.
	 */
	public void renderDebug(ShapeRenderer shapeRenderer) {
		animations.renderDebug(shapeRenderer);
	}

	/**
	 * Disposes this Entity in {@link EntityManager}
	 *
	 * @param alpha sets this Entity alpha before disposing.
	 */
	public void dispose(float alpha) {
		getAlpha().set(alpha);
		dispose = true;
	}

	/**
	 * @param renderTask will be called when Entity is rendering.
	 */
	public void setRenderTask(RenderTask renderTask) {
		this.renderTask = renderTask;
	}

	/**
	 * @param listener adds a listener to this Entity's {@link States}
	 */
	protected void addStateListener(StateListener<S> listener) {
		states.addListener(listener);
	}

	/**
	 * @param input a Input enum
	 * @return whether the Input is valid
	 */
	protected abstract boolean canInput(I input);

	/**
	 * @param input Input into {@link States}
	 * @return wheter the Input was valid
	 */
	public boolean input(I input) {
		if (!isDispose() && canInput(input)) {
			return states.input(input);
		}
		return false;
	}

	/* Getters */
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

	public MutableFloat getAlpha() {
		return data.getAlpha();
	}

	public Color getColor() {
		return data.getColor();
	}

	public GameScreen getGame() {
		return game;
	}
}
