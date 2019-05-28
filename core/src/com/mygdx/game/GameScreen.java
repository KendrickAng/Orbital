package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.entity.Boss1;

import static com.mygdx.game.CharacterType.TANK;

public class GameScreen implements Screen {
	// Game reference.
	private MyGdxGame game;

	// For debugging.
	private ShapeRenderer shapeBatch;

	private CharacterController controller;
	private Boss1 boss;
	private Background background;

	public GameScreen(MyGdxGame game) {
		// init rectangle to (0, 0)
		this.game = game;
		this.controller = new CharacterController(game);
		controller.setCharacter(TANK);
		boss = new Boss1(game);

		this.background = new Background(game.getTextureManager());
		this.shapeBatch = new ShapeRenderer();
		shapeBatch.setColor(Color.GOLD);
	}

	@Override
	public void show() {
		Gdx.app.log("GameScreen.java", "show() called");
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		OrthographicCamera camera = game.getCamera();
		SpriteBatch batch = game.getSpriteBatch();

		camera.update();

		/* Render */
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		background.render(batch);
		boss.render(batch);
		// render interactive entities (shuriken)
		game.getEntityManager().renderAll(batch);
		controller.character().render(batch);
		batch.end();

		/* Debug */
		shapeBatch.setProjectionMatrix(camera.combined);
		shapeBatch.begin(ShapeRenderer.ShapeType.Line);
		controller.character().renderDebug(shapeBatch);
		shapeBatch.end();
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
		game.dispose();
		shapeBatch.dispose();
	}
}
