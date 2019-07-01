package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Background;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.entity.EntityManager;
import com.mygdx.game.entity.boss1.Boss1;
import com.mygdx.game.entity.boss1.Boss1AI;
import com.mygdx.game.entity.boss1.Boss1Controller;
import com.mygdx.game.entity.character.Assassin;
import com.mygdx.game.entity.character.Character;
import com.mygdx.game.entity.character.CharacterController;
import com.mygdx.game.entity.character.CharacterInput;
import com.mygdx.game.entity.character.Tank;
import com.mygdx.game.entity.healthbar.AssassinBar;
import com.mygdx.game.entity.healthbar.BossBar;
import com.mygdx.game.entity.healthbar.TankBar;

import static com.mygdx.game.MyGdxGame.BOSS1_AI;
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

	private TankBar tankBar;
	private AssassinBar assassinBar;
	private BossBar bossBar;

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
		if (BOSS1_AI) {
			new Boss1AI(this);
		}
		this.playerController = new CharacterController(this);
		Boss1Controller bossController = new Boss1Controller(this);

		// An input multiplexer sends input to both controllers at once.
		InputMultiplexer inputMultiplexer = game.getInputMultiplexer();
		inputMultiplexer.addProcessor(playerController);
		inputMultiplexer.addProcessor(bossController);

		this.background = new Background(game.getTextureManager());
		this.shapeRenderer = new ShapeRenderer();
		shapeRenderer.setColor(Color.GOLD);

		tankBar = new TankBar(tank);
		assassinBar = new AssassinBar(assassin);
		bossBar = new BossBar(boss1);
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

		// TODO: Abstract these
		// TODO: Fix dispose
		if (character == tank && tank.isDispose()) {
			switchCharacter();
		}

		if (character == assassin && assassin.isDispose()) {
			switchCharacter();
		}

		/* Render */
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		background.render(batch);
		entityManager.renderAll(batch);

		if (character == tank && !tank.isDispose()) {
			tankBar.render(batch);
		} else if (character == assassin && !assassin.isDispose()) {
			assassinBar.render(batch);
		}

		if (!boss1.isDispose()) {
			bossBar.render(batch);
		}
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

	public void switchCharacter() {
		if (character != null && character.useSwitchCharacter()) {
			Character next;
			if (character == tank && !assassin.isDispose()) {
				next = assassin;
			} else if (character == assassin && !tank.isDispose()) {
				next = tank;
			} else {
				return;
			}

			character.setVisible(false);
			float x = character.getPosition().x;
			boolean flipX = character.getFlipX().get();

			character = next;
			for (CharacterInput input : playerController.getInputs()) {
				character.input(input);
			}
			character.setVisible(true);
			character.getPosition().x = x;
			character.getPosition().y = MAP_HEIGHT;
			character.getFlipX().set(flipX);

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
