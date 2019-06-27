package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.entity.CollisionManager;
import com.mygdx.game.entity.boss1.Boss1AI;
import com.mygdx.game.entity.character.Assassin;
import com.mygdx.game.entity.boss1.Boss1;
import com.mygdx.game.entity.boss1.Boss1Controller;
import com.mygdx.game.entity.character.Character;
import com.mygdx.game.entity.character.CharacterController;
import com.mygdx.game.entity.EntityManager;
import com.mygdx.game.entity.character.Tank;
import com.mygdx.game.texture.TextureManager;

import static com.mygdx.game.MyGdxGame.DEBUG;

public class GameScreen implements Screen {
	private static final boolean ACTIVATE_AI = false; // switch to activate boss ACTIVATE_AI.

	// Game reference.
	private MyGdxGame game;

	// For debugging.
	private ShapeRenderer shapeRenderer;

	private CharacterController playerController;
	private EntityManager entityManager;
	private CollisionManager collisionManager;
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
		this.collisionManager = new CollisionManager(this);

		/* Entities */
		// Order of render in entityManager depends on order of Entity() creation.
		this.boss1 = new Boss1(this);

		this.tank = new Tank(this);
		this.assassin = new Assassin(this);
		assassin.setVisible(false);
		this.character = tank;

		/* Input */
		if (ACTIVATE_AI) new Boss1AI(this);
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

		/* Collision detection */
		collisionManager.colliding(character, boss1);

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

	public void switchCharacter() {
		Gdx.app.log("GameScreen.java", "Switched Characters");
		Character prev = character;
		Character next = prev.equals(tank) ? assassin : tank;
		prev.setVisible(false);
		next.setPosition(prev.getPosition());
		next.setVelocity(prev.getVelocity());
		next.setSpriteDirection(prev.getSpriteDirection());
		next.setInputDirection(prev.getInputDirection());
		next.setVisible(true);

		character = next;
		playerController.update();
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

	public TextureManager getTextureManager() {
		return game.getTextureManager();
	}
}
