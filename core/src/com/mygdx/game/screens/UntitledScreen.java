package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.UntitledGame;

import static com.mygdx.game.UntitledGame.DEBUG;

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

	public abstract void render(SpriteBatch batch);

	public abstract void renderDebug(ShapeRenderer renderer);

	public abstract void pauseScreen();

	public abstract void resumeScreen();

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (running) {
			this.render(game.getSpriteBatch());

			if (DEBUG) {
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

	void setScreen(ScreenName screen) {
		game.setScreen(screen);
	}
}
