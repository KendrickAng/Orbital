package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.entity.EntityManager;
import com.mygdx.game.entity.boss1.Boss1;
import com.mygdx.game.entity.boss1.Boss1AI;
import com.mygdx.game.entity.boss1.Boss1Controller;
import com.mygdx.game.entity.character.Assassin;
import com.mygdx.game.entity.character.Character;
import com.mygdx.game.entity.character.CharacterController;
import com.mygdx.game.entity.character.CharacterInput;
import com.mygdx.game.entity.character.Tank;

import java.util.HashSet;

import static com.mygdx.game.MyGdxGame.DEBUG;
import static com.mygdx.game.MyGdxGame.MAP_HEIGHT;

public class GameScreen implements Screen {
	// Game reference.
	private MyGdxGame game;

	// For debugging.
	private ShapeRenderer shapeRenderer;

	private CharacterController playerController;
	private EntityManager entityManager;
	private Background background;

	/* Entities */
	private Character character;
	private Tank tank;
	private Assassin assassin;
	private Boss1 boss1;

	public GameScreen(MyGdxGame game) {
		// init rectangle to (0, 0)
		this.game = game;
		this.entityManager = new EntityManager();

		/* Entities */
		// Order of render in entityManager depends on order of Entity() creation.
		this.boss1 = new Boss1(this);

		this.tank = new Tank(this);
		this.assassin = new Assassin(this);
		assassin.setVisible(false);
		this.character = tank;

		/* Input */
		new Boss1AI(this);
		this.playerController = new CharacterController(this);
		Boss1Controller bossController = new Boss1Controller(this);

		// An input multiplexer sends input to both controllers at once.
		InputMultiplexer inputMultiplexer = new InputMultiplexer(playerController, bossController);
		Gdx.input.setInputProcessor(inputMultiplexer);

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
		if (DEBUG) {
			shapeRenderer.setProjectionMatrix(camera.combined);
			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			entityManager.renderDebugAll(shapeRenderer);
			shapeRenderer.end();
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
		game.dispose();
		shapeRenderer.dispose();
	}

	public void switchCharacter(HashSet<CharacterInput> inputs) {
		if (character.useSwitchCharacter()) {
			character.setVisible(false);
			float x = character.getPosition().x;
			boolean flipX = character.getFlipX().get();

			Character next = character.equals(tank) ? assassin : tank;
			for (CharacterInput input : inputs) {
				next.input(input);
			}
			next.setVisible(true);
			next.getPosition().x = x;
			next.getPosition().y = MAP_HEIGHT;
			next.getFlipX().set(flipX);

			character = next;
			playerController.update();
			Gdx.app.log("GameScreen.java", "Switched Characters");
		}
	}

	/* Getters */
	public Character getCharacter() {
		return character;
	}

	public Boss1 getBoss1() {
		return boss1;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}
}
