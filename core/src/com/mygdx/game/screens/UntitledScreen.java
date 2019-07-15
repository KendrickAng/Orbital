package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.UntitledGame;

import static com.mygdx.game.UntitledGame.DEBUG;

public abstract class UntitledScreen implements Screen {
	private UntitledGame game;

	public UntitledScreen(UntitledGame game) {
		this.game = game;
	}

	@Override
	public void show() {

	}

	public abstract void update();

	public abstract void render(SpriteBatch batch);

	public abstract void renderDebug(ShapeRenderer renderer);

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		OrthographicCamera camera = game.getCamera();
		camera.update();
		update();

		SpriteBatch batch = game.getSpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		this.render(batch);
		batch.end();

		if (DEBUG) {
			ShapeRenderer renderer = game.getShapeRenderer();
			renderer.setProjectionMatrix(camera.combined);
			renderer.begin(ShapeRenderer.ShapeType.Line);
			this.renderDebug(renderer);
			renderer.end();
		}
	}

	@Override
	public void resize(int width, int height) {
		game.getViewport().update(width, height);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}

	void setScreen(ScreenName screen) {
		game.getInputMultiplexer().clear();
		switch (screen) {
			case MAIN_MENU:
				game.setScreen(new MainMenuScreen(game));
				break;
			case CONTROLS:
				game.setScreen(new ControlsScreen(game));
				break;
			case GAME:
				game.setScreen(new GameScreen(game));
				break;
		}
	}
}
