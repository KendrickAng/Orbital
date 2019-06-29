package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.ui.ContentAlignment;
import com.mygdx.game.ui.Text;

import static com.mygdx.game.MyGdxGame.*;
import static com.mygdx.game.ui.ContentAlignment.CENTER;

public class MainMenuScreen implements Screen {
	private MyGdxGame game;
	private Text title;

	public MainMenuScreen(MyGdxGame game) {
		this.game = game;
		this.title = new Text(TITLE, TITLE_FONTSIZE)
						.setX(GAME_WIDTH / 2f)
						.setY(GAME_HEIGHT/ 2f)
						.setAlignment(CENTER)
						.attach(game);
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
		this.title.render();
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
