package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.mygdx.game.MyGdxGame.GAME_HEIGHT;
import static com.mygdx.game.MyGdxGame.GAME_WIDTH;

public class MainMenuScreen implements Screen {
	private MyGdxGame game;

	public MainMenuScreen(MyGdxGame game) {
		this.game = game;
	}

	@Override
	public void show() {
		Gdx.app.log("MainMenuScreen.java", "show() called");
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		OrthographicCamera camera = game.getCamera();
		camera.update();

		SpriteBatch batch = game.getSpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		game.getFont().draw(batch, "Press any key", GAME_WIDTH / 2, GAME_HEIGHT / 2);
		batch.end();

		if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
			game.setScreen(new GameScreen(game));
			dispose();
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
}
