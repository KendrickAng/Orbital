package com.mygdx.game.screens.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MutableBoolean;
import com.mygdx.game.MutableFloat;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.game.animation.Animations;
import com.mygdx.game.screens.game.state.StateListener;
import com.mygdx.game.screens.game.state.States;

/**
 * Represents all renderable, interactive objects such as throwing stars/falling rocks.
 * LivingEntity represents Characters, Bosses.
 */
public abstract class Entity<I extends Enum, S extends Enum, P extends Enum> {
	public static final float GRAVITY = -3f;

	private GameScreen game;
	private EntityData data;
	private RenderTask renderTask;

	private States<I, S> states;
	private Animations<S, P> animations;

	private boolean dispose;

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

	protected abstract void defineStates(States<I, S> states);

	protected abstract void defineAnimations(Animations<S, P> animations, Assets assets);

	public void render(SpriteBatch batch) {
		states.update();
		animations.render(batch);

		if (renderTask != null) {
			renderTask.render(batch);
		}
	}

	public void renderDebug(ShapeRenderer shapeRenderer) {
		animations.renderDebug(shapeRenderer);
	}

	public void dispose(float alpha) {
		getAlpha().set(alpha);
		dispose = true;
	}

	/* Setters */
	public void setRenderTask(RenderTask renderTask) {
		this.renderTask = renderTask;
	}

	protected void addStateListener(StateListener<S> listener) {
		states.addListener(listener);
	}

	protected abstract boolean canInput(I input);

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
