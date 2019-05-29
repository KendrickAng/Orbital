package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.entity.Assassin;
import com.mygdx.game.entity.Boss1;
import com.mygdx.game.entity.Character;
import com.mygdx.game.entity.CharacterController;
import com.mygdx.game.entity.EntityManager;
import com.mygdx.game.entity.Tank;
import com.mygdx.game.texture.TextureManager;

public class GameScreen implements Screen {
	// Game reference.
	private MyGdxGame game;

	// For debugging.
	private ShapeRenderer shapeRenderer;

	private CharacterController controller;
	private EntityManager entityManager;
	private Background background;

	/* Entities */
	private Boss1 boss;
	private Tank tank;
	private Assassin assassin;

	public GameScreen(MyGdxGame game) {
		// init rectangle to (0, 0)
		this.game = game;
		this.entityManager = new EntityManager();

		/* Entities */
		this.boss = new Boss1(this);
		this.tank = new Tank(this);
		this.assassin = new Assassin(this);
		assassin.setVisible(false);

		this.controller = new CharacterController(this);
		controller.setCharacter(tank);

		this.background = new Background(game.getTextureManager());
		this.shapeRenderer = new ShapeRenderer();
		shapeRenderer.setColor(Color.GOLD);
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
		entityManager.renderAll(batch);
		batch.end();

		/* Debug */
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		entityManager.renderDebugAll(shapeRenderer);
		shapeRenderer.end();
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
		shapeRenderer.dispose();
	}

	public void switchCharacter() {
		Gdx.app.log("GameScreen.java", "Switched Characters");
		Character prev = controller.getCharacter();
		Character next = prev.equals(tank) ? assassin : tank;
		next.setPosition(prev.getPosition());
		next.setVelocity(prev.getVelocity());
		next.setSpriteDirection(prev.getSpriteDirection());
		next.setInputDirection(prev.getInputDirection());

		controller.setCharacter(next);
		prev.setVisible(false);
		next.setVisible(true);
	}

	/* Getters */
	public Boss1 getBoss() {
		return boss;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public TextureManager getTextureManager() {
		return game.getTextureManager();
	}
}
