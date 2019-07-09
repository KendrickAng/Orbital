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
import com.mygdx.game.UntitledGame;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.entity.EntityManager;
import com.mygdx.game.entity.boss1.Boss1;
import com.mygdx.game.entity.boss1.Boss1AI;
import com.mygdx.game.entity.boss1.Boss1Controller;
import com.mygdx.game.entity.character.Assassin;
import com.mygdx.game.entity.character.Character;
import com.mygdx.game.entity.character.CharacterController;
import com.mygdx.game.entity.character.Tank;
import com.mygdx.game.ui.Cooldowns;
import com.mygdx.game.ui.HealthBar;

import static com.mygdx.game.UntitledGame.BOSS1_AI;
import static com.mygdx.game.UntitledGame.DEBUG;
import static com.mygdx.game.UntitledGame.GAME_HEIGHT;
import static com.mygdx.game.UntitledGame.GAME_WIDTH;
import static com.mygdx.game.UntitledGame.MAP_HEIGHT;
import static com.mygdx.game.assets.Assets.TextureName.COOLDOWN_BLOCK;
import static com.mygdx.game.assets.Assets.TextureName.COOLDOWN_CLEANSE;
import static com.mygdx.game.assets.Assets.TextureName.COOLDOWN_DASH;
import static com.mygdx.game.assets.Assets.TextureName.COOLDOWN_FORTRESS;
import static com.mygdx.game.assets.Assets.TextureName.COOLDOWN_IMPALE;
import static com.mygdx.game.assets.Assets.TextureName.COOLDOWN_SHURIKEN_THROW;
import static com.mygdx.game.assets.Assets.TextureName.HEALTH_BAR_ASSASSIN;
import static com.mygdx.game.assets.Assets.TextureName.HEALTH_BAR_BACKGROUND;
import static com.mygdx.game.assets.Assets.TextureName.HEALTH_BAR_BOSS;
import static com.mygdx.game.assets.Assets.TextureName.HEALTH_BAR_TANK;

public class GameScreen implements Screen {
	private static final float INFO_PADDING = 20f;
	private static final float COOLDOWNS_Y = GAME_HEIGHT - 50;

	private UntitledGame game;
	private Assets assets;

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

	private HealthBar tankBar;
	private HealthBar assassinBar;
	private HealthBar bossBar;

	private Cooldowns tankCooldowns;
	private Cooldowns assassinCooldowns;

	public GameScreen(UntitledGame game) {
		// init rectangle to (0, 0)
		this.game = game;
		this.assets = game.getAssets();
		this.entityManager = new EntityManager();

		/* Entities */
		this.tank = new Tank(this);
		this.assassin = new Assassin(this);
		this.assassin.setVisible(false);
		this.character = tank;
		this.boss1 = new Boss1(this);

		/* Input */
		this.playerController = new CharacterController(this);

		// An input multiplexer sends input to both controllers at once.
		InputMultiplexer inputMultiplexer = game.getInputMultiplexer();
		inputMultiplexer.addProcessor(playerController);
		if (BOSS1_AI) {
			new Boss1AI(this);
		} else {
			inputMultiplexer.addProcessor(new Boss1Controller(this));
		}

		this.background = new Background(game.getAssets());
		this.shapeRenderer = new ShapeRenderer();
		shapeRenderer.setColor(Color.GOLD);

		/* Health Bars */
		tankBar = new HealthBar(tank, assets.getTexture(HEALTH_BAR_TANK), assets.getTexture(HEALTH_BAR_BACKGROUND))
				.setX(INFO_PADDING)
				.setY(GAME_HEIGHT - INFO_PADDING)
				.setWidth((float) GAME_WIDTH / 2);

		assassinBar = new HealthBar(assassin, assets.getTexture(HEALTH_BAR_ASSASSIN), assets.getTexture(HEALTH_BAR_BACKGROUND))
				.setX(INFO_PADDING)
				.setY(GAME_HEIGHT - INFO_PADDING)
				.setWidth((float) GAME_WIDTH / 2);

		bossBar = new HealthBar(boss1, assets.getTexture(HEALTH_BAR_BOSS), assets.getTexture(HEALTH_BAR_BACKGROUND))
				.setX(INFO_PADDING)
				.setY(INFO_PADDING)
				.setWidth(GAME_WIDTH - INFO_PADDING * 2);

		tankCooldowns = new Cooldowns(game.getAssets())
				.setX(INFO_PADDING)
				.setY(COOLDOWNS_Y)
				.add(tank.getBlockState(), game.getAssets().getTexture(COOLDOWN_BLOCK))
				.add(tank.getImpaleState(), game.getAssets().getTexture(COOLDOWN_IMPALE))
				.add(tank.getFortressState(), game.getAssets().getTexture(COOLDOWN_FORTRESS));

		assassinCooldowns = new Cooldowns(game.getAssets())
				.setX(INFO_PADDING)
				.setY(COOLDOWNS_Y)
				.add(assassin.getDashState(), game.getAssets().getTexture(COOLDOWN_DASH))
				.add(assassin.getShurikenState(), game.getAssets().getTexture(COOLDOWN_SHURIKEN_THROW))
				.add(assassin.getCleanseState(), game.getAssets().getTexture(COOLDOWN_CLEANSE));
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
			tankCooldowns.render(batch);
		} else if (character == assassin && !assassin.isDispose()) {
			assassinBar.render(batch);
			assassinCooldowns.render(batch);
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

	public CharacterController getPlayerController() {
		return playerController;
	}

	public void switchCharacter() {
		if (character.useSwitchCharacter() || character.isDispose()) {
			Character next;
			if (character == tank && !assassin.isDispose()) {
				next = assassin;
			} else if (character == assassin && !tank.isDispose()) {
				next = tank;
			} else {
				character.endCrowdControl();
				return;
			}

			character.setVisible(false);
			float x = character.getPosition().x;
			boolean flipX = character.getFlipX().get();

			character = next;
			character.endCrowdControl();
			character.setVisible(true);
			character.getPosition().x = x;
			character.getPosition().y = MAP_HEIGHT;
			character.getFlipX().set(flipX);

			playerController.update();
//			Gdx.app.log("GameScreen.java", "Switched Characters");
		}
	}

	/* Getters */
	public Tank getTank() {
		return tank;
	}

	public Assassin getAssassin() {
		return assassin;
	}

	public Character getCharacter() {
		return character;
	}

	public Boss1 getBoss1() {
		return boss1;
	}

	public Assets getAssets() {
		return assets;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}
}
