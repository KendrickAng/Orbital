package com.untitled.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.untitled.UntitledGame;

/**
 * An abstract Screen of Untitled.
 * Abstracts common methods.
 */
public abstract class UntitledScreen implements Screen {
	private boolean running;
	private UntitledGame game;

	public UntitledScreen(UntitledGame game) {
		this.game = game;
		this.running = true;
	}

	@Override
	public void show() {

	}

	/**
	 * @param batch {@link SpriteBatch} to render this Screen on.
	 */
	public abstract void render(SpriteBatch batch);

	/**
	 * @param renderer {@link ShapeRenderer} to render debug this Screen on.
	 */
	public abstract void renderDebug(ShapeRenderer renderer);

	/**
	 * Called when this Screen is paused. (LibGDX lifecycle)
	 */
	public abstract void pauseScreen();

	/**
	 * Called when this Screen is resumed. (LibGDX lifecycle)
	 */
	public abstract void resumeScreen();

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (running) {
			this.render(game.getSpriteBatch());

			if (UntitledGame.DEBUG_HITBOXES) {
				this.renderDebug(game.getShapeRenderer());
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		game.getViewport().update(width, height);
	}

	@Override
	public void pause() {
		this.running = false;
		pauseScreen();
	}

	@Override
	public void resume() {
		this.running = true;
		resumeScreen();
	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}

	protected void setScreen(ScreenName screen) {
		game.setScreen(screen);
	}
}
