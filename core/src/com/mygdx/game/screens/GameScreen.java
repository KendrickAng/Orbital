package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.UntitledGame;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.assets.MusicName;
import com.mygdx.game.highscores.Highscores;
import com.mygdx.game.screens.game.Background;
import com.mygdx.game.screens.game.EntityManager;
import com.mygdx.game.screens.game.FloatingTextManager;
import com.mygdx.game.screens.game.Floor;
import com.mygdx.game.screens.game.ability.CooldownState;
import com.mygdx.game.screens.game.boss1.Boss1;
import com.mygdx.game.screens.game.boss1.Boss1AI;
import com.mygdx.game.screens.game.boss1.Boss1Controller;
import com.mygdx.game.screens.game.character.Assassin;
import com.mygdx.game.screens.game.character.Character;
import com.mygdx.game.screens.game.character.CharacterController;
import com.mygdx.game.screens.game.character.Tank;
import com.mygdx.game.screens.game.debuff.Debuff;
import com.mygdx.game.ui.ButtonUI;
import com.mygdx.game.ui.Cooldowns;
import com.mygdx.game.ui.HealthBar;
import com.mygdx.game.ui.StackBar;
import com.mygdx.game.ui.TextUI;
import com.mygdx.game.ui.TextUIAlign;

import static com.mygdx.game.UntitledGame.BUTTON_H;
import static com.mygdx.game.UntitledGame.BUTTON_W;
import static com.mygdx.game.UntitledGame.CAMERA_HEIGHT;
import static com.mygdx.game.UntitledGame.CAMERA_WIDTH;
import static com.mygdx.game.UntitledGame.DEBUG_BOSS_AI;
import static com.mygdx.game.UntitledGame.SETTINGS_MUSIC_VOLUME;
import static com.mygdx.game.UntitledGame.SETTINGS_MUSIC_VOLUME_DEFAULT;
import static com.mygdx.game.assets.FontName.MINECRAFT_16;
import static com.mygdx.game.assets.FontName.MINECRAFT_8;
import static com.mygdx.game.assets.TextureName.BUTTON_HOVER;
import static com.mygdx.game.assets.TextureName.BUTTON_NORMAL;
import static com.mygdx.game.assets.TextureName.COOLDOWN_BLOCK;
import static com.mygdx.game.assets.TextureName.COOLDOWN_CLEANSE;
import static com.mygdx.game.assets.TextureName.COOLDOWN_DASH;
import static com.mygdx.game.assets.TextureName.COOLDOWN_FORTRESS;
import static com.mygdx.game.assets.TextureName.COOLDOWN_IMPALE;
import static com.mygdx.game.assets.TextureName.COOLDOWN_SHURIKEN_THROW;
import static com.mygdx.game.assets.TextureName.COOLDOWN_SWITCH_CHARACTER;
import static com.mygdx.game.assets.TextureName.GAME_OVERLAY;
import static com.mygdx.game.assets.TextureName.HEALTH_BAR_ASSASSIN;
import static com.mygdx.game.assets.TextureName.HEALTH_BAR_BOSS;
import static com.mygdx.game.assets.TextureName.HEALTH_BAR_TANK;
import static com.mygdx.game.assets.TextureName.INFO_BAR_BACKGROUND;
import static com.mygdx.game.assets.TextureName.STACK_BAR_ASSASSIN;
import static com.mygdx.game.screens.ScreenName.MAIN_MENU;
import static com.mygdx.game.screens.game.ability.CooldownState.COOLDOWN_STATES;
import static com.mygdx.game.screens.game.debuff.DebuffType.DAMAGE_REDUCTION;
import static com.mygdx.game.ui.UIAlign.BOTTOM_LEFT;
import static com.mygdx.game.ui.UIAlign.BOTTOM_RIGHT;
import static com.mygdx.game.ui.UIAlign.MIDDLE;
import static com.mygdx.game.ui.UIAlign.TOP_LEFT;
import static com.mygdx.game.ui.UIAlign.TOP_RIGHT;

public class GameScreen extends UntitledScreen {
	// Game Size
	public static final int GAME_WIDTH = CAMERA_WIDTH;
	public static final int GAME_HEIGHT = CAMERA_HEIGHT;
	public static final float GAME_FLOOR_HEIGHT = 60f;

	private static final String TANK_TEXT = "TANK";
	private static final String ASSASSIN_TEXT = "ASSASSIN";

	private static final float CHARACTER_TEXT_X = 20f;
	private static final float CHARACTER_TEXT_Y = CAMERA_HEIGHT - 20f;

	private static final float CHARACTER_HEALTH_BAR_X = CHARACTER_TEXT_X;
	private static final float CHARACTER_HEALTH_BAR_Y = CHARACTER_TEXT_Y - 10f;
	private static final float CHARACTER_HEALTH_BAR_W = CAMERA_WIDTH / 2f;

	private static final float TANK_COOLDOWNS_X = CHARACTER_HEALTH_BAR_X;
	private static final float TANK_COOLDOWNS_Y = CHARACTER_HEALTH_BAR_Y - 16f;

	private static final float ASSASSIN_STACK_BAR_X = CHARACTER_HEALTH_BAR_X;
	private static final float ASSASSIN_STACK_BAR_Y = CHARACTER_HEALTH_BAR_Y - 10f;
	private static final float ASSASSIN_STACK_BAR_W = CAMERA_WIDTH / 4f;

	private static final float ASSASSIN_COOLDOWNS_X = CHARACTER_HEALTH_BAR_X;
	private static final float ASSASSIN_COOLDOWNS_Y = ASSASSIN_STACK_BAR_Y - 16f;

	private static final float BOSS_BAR_X = 20f;
	private static final float BOSS_BAR_Y = 20f;
	private static final float BOSS_BAR_W = CAMERA_WIDTH - 40f;

	private static final String BOSS_TEXT = "BOSS - GOLEM";
	private static final float BOSS_TEXT_X = 20f;
	private static final float BOSS_TEXT_Y = 32f;

	private static final String FPS_TEXT = "FPS: ";
	private static final float FPS_TEXT_X = CAMERA_WIDTH - 5f;
	private static final float FPS_TEXT_Y = 5f;

	private static final String LEVEL_TEXT = "LEVEL: ";
	private static final float LEVEL_TEXT_X = CAMERA_WIDTH - 20f;
	private static final float LEVEL_TEXT_Y = CAMERA_HEIGHT - 20f;

	private static final String TIME_TEXT = "TIME: ";
	private static final float TIME_TEXT_X = CAMERA_WIDTH - 20f;
	private static final float TIME_TEXT_Y = LEVEL_TEXT_Y - 14f;

	private static final String SCORE_TEXT = "SCORE: ";
	private static final float SCORE_TEXT_X = CAMERA_WIDTH - 20f;
	private static final float SCORE_TEXT_Y = TIME_TEXT_Y - 14f;

	private static final String HIGHSCORE_UPLOADING_TEXT = "UPLOADING HIGHSCORE...";
	private static final String HIGHSCORE_SUCCESS_TEXT = "UPLOAD SUCCESSFUL!";
	private static final String HIGHSCORE_FAILED_TEXT = "UPLOAD FAILED...";
	private static final float HIGHSCORE_TEXT_X = CAMERA_WIDTH - 20f;
	private static final float HIGHSCORE_TEXT_Y = SCORE_TEXT_Y - 14f;

	private static final String GAME_OVER_LOSE_TEXT = "GAME OVER...";
	private static final String GAME_OVER_WIN_TEXT = "YOU DID IT!\n\nMORE BOSSES COMING SOON (TM)";
	private static final float GAME_OVER_TEXT_X = CAMERA_WIDTH / 2f;
	private static final float GAME_OVER_TEXT_Y = CAMERA_HEIGHT / 2f + 50f;

	private static final String EXIT_TEXT = "EXIT";
	private static final float EXIT_TEXT_X = CAMERA_WIDTH / 2f;
	private static final float EXIT_TEXT_Y = CAMERA_HEIGHT / 2f - 50f;

	private static final float SWITCH_CHARACTER_COOLDOWN = 2f;
	private static final float DEAD_CHARACTER_INVULNERABLE_DURATION = 1f;
	private static final float GAME_OVER_TRANSITION_DURATION = 2f;

	/* Variables */
	private int score;
	private int level;
	private float time;

	private Assets A;
	private InputMultiplexer multiplexer;

	private CooldownState switchCharacter;
	private CharacterController playerController;
	private Debuff deadCharacterDebuff;
	private EntityManager entityManager;
	private FloatingTextManager floatingTextManager;
	private Highscores highscores;
	private Timer timer;
	private State state;

	private OrthographicCamera cameraGame;
	private float cameraX;
	private OrthographicCamera cameraUI;
	private FitViewport viewportUI;

	private Background background;
	private Floor floor;

	/* Entities */
	private Character character;
	private Tank tank;
	private Assassin assassin;
	private Boss1 boss1;

	/* UI */
	private TextUI tankText;
	private HealthBar tankHealthBar;
	private Cooldowns tankCooldowns;

	private TextUI assassinText;
	private HealthBar assassinHealthBar;
	private StackBar assassinStackBar;
	private Cooldowns assassinCooldowns;

	private TextUI bossText;
	private HealthBar bossHealthBar;

	private TextUI fpsText;
	private TextUI levelText;
	private TextUI scoreText;
	private TextUI timeText;
	private TextUI highscoreText;

	private TextUI gameOverText;
	private TextUI exitText;
	private ButtonUI exitButton;

	private enum State {
		BOSS_FIGHT, GAME_OVER_TRANSITION, GAME_OVER_SCREEN
	}

	public GameScreen(UntitledGame game) {
		super(game);

		this.A = game.getAssets();
		this.multiplexer = game.getInputMultiplexer();

		this.cameraGame = game.getCamera();
		this.cameraX = cameraGame.position.x;
		this.cameraUI = new OrthographicCamera();
		this.cameraUI.setToOrtho(false, CAMERA_WIDTH, CAMERA_HEIGHT);
		this.viewportUI = new FitViewport(CAMERA_WIDTH, CAMERA_HEIGHT, cameraUI);

		this.highscores = game.getHighscores();
		this.entityManager = new EntityManager();
		this.floatingTextManager = new FloatingTextManager(A);
		this.timer = new Timer();
		this.state = State.BOSS_FIGHT;

		this.switchCharacter = new CooldownState(SWITCH_CHARACTER_COOLDOWN);
		this.deadCharacterDebuff = new Debuff(DAMAGE_REDUCTION, 1f, DEAD_CHARACTER_INVULNERABLE_DURATION);

		/* Entities */
		this.tank = new Tank(this);
		this.assassin = new Assassin(this);
		this.assassin.getAlpha().set(0);
		this.character = tank;
		this.boss1 = new Boss1(this);

		/* Input */
		this.playerController = new CharacterController(this);

		multiplexer.addProcessor(playerController);
		if (DEBUG_BOSS_AI) {
			multiplexer.addProcessor(new Boss1Controller(this));
		} else {
			new Boss1AI(this);
		}

		this.background = new Background(A);
		this.floor = new Floor(A);

		/* UI */
		// Tank
		tankText = new TextUI(TOP_LEFT, A.getFont(MINECRAFT_8))
				.setX(CHARACTER_TEXT_X)
				.setY(CHARACTER_TEXT_Y)
				.setText(TANK_TEXT);

		tankHealthBar = new HealthBar(TOP_LEFT, tank, A.getTexture(HEALTH_BAR_TANK), A.getTexture(INFO_BAR_BACKGROUND))
				.setX(CHARACTER_HEALTH_BAR_X)
				.setY(CHARACTER_HEALTH_BAR_Y)
				.setW(CHARACTER_HEALTH_BAR_W);

		tankCooldowns = new Cooldowns(TOP_LEFT, A)
				.setX(TANK_COOLDOWNS_X)
				.setY(TANK_COOLDOWNS_Y)
				.add(tank.getBlockState(), A.getTexture(COOLDOWN_BLOCK))
				.add(tank.getImpaleState(), A.getTexture(COOLDOWN_IMPALE))
				.add(tank.getFortressState(), A.getTexture(COOLDOWN_FORTRESS))
				.add(switchCharacter, A.getTexture(COOLDOWN_SWITCH_CHARACTER));

		// Assassin
		assassinText = new TextUI(TOP_LEFT, A.getFont(MINECRAFT_8))
				.setX(CHARACTER_TEXT_X)
				.setY(CHARACTER_TEXT_Y)
				.setText(ASSASSIN_TEXT);

		assassinHealthBar = new HealthBar(TOP_LEFT, assassin, A.getTexture(HEALTH_BAR_ASSASSIN), A.getTexture(INFO_BAR_BACKGROUND))
				.setX(CHARACTER_HEALTH_BAR_X)
				.setY(CHARACTER_HEALTH_BAR_Y)
				.setW(CHARACTER_HEALTH_BAR_W);

		assassinStackBar = new StackBar(TOP_LEFT, assassin, A.getTexture(STACK_BAR_ASSASSIN), A.getTexture(INFO_BAR_BACKGROUND))
				.setX(ASSASSIN_STACK_BAR_X)
				.setY(ASSASSIN_STACK_BAR_Y)
				.setW(ASSASSIN_STACK_BAR_W);

		assassinCooldowns = new Cooldowns(TOP_LEFT, A)
				.setX(ASSASSIN_COOLDOWNS_X)
				.setY(ASSASSIN_COOLDOWNS_Y)
				.add(assassin.getDashState(), A.getTexture(COOLDOWN_DASH))
				.add(assassin.getShurikenState(), A.getTexture(COOLDOWN_SHURIKEN_THROW))
				.add(assassin.getCleanseState(), A.getTexture(COOLDOWN_CLEANSE))
				.add(switchCharacter, A.getTexture(COOLDOWN_SWITCH_CHARACTER));

		// Boss
		bossText = new TextUI(BOTTOM_LEFT, A.getFont(MINECRAFT_8))
				.setX(BOSS_TEXT_X)
				.setY(BOSS_TEXT_Y)
				.setText(BOSS_TEXT);

		bossHealthBar = new HealthBar(BOTTOM_LEFT, boss1, A.getTexture(HEALTH_BAR_BOSS), A.getTexture(INFO_BAR_BACKGROUND))
				.setX(BOSS_BAR_X)
				.setY(BOSS_BAR_Y)
				.setW(BOSS_BAR_W);

		fpsText = new TextUI(BOTTOM_RIGHT, A.getFont(MINECRAFT_8))
				.setX(FPS_TEXT_X)
				.setY(FPS_TEXT_Y)
				.setColor(Color.GRAY);

		levelText = new TextUI(TOP_RIGHT, A.getFont(MINECRAFT_8))
				.setX(LEVEL_TEXT_X)
				.setY(LEVEL_TEXT_Y);

		timeText = new TextUI(TOP_RIGHT, A.getFont(MINECRAFT_8))
				.setX(TIME_TEXT_X)
				.setY(TIME_TEXT_Y);

		scoreText = new TextUI(TOP_RIGHT, A.getFont(MINECRAFT_8))
				.setX(SCORE_TEXT_X)
				.setY(SCORE_TEXT_Y);

		highscoreText = new TextUI(TOP_RIGHT, A.getFont(MINECRAFT_8))
				.setX(HIGHSCORE_TEXT_X)
				.setY(HIGHSCORE_TEXT_Y)
				.setColor(Color.GOLD);

		exitText = new TextUI(MIDDLE, A.getFont(MINECRAFT_8))
				.setX(EXIT_TEXT_X)
				.setY(EXIT_TEXT_Y)
				.setText(EXIT_TEXT);

		exitButton = new ButtonUI(MIDDLE, this.viewportUI, () -> setScreen(MAIN_MENU))
				.setX(EXIT_TEXT_X)
				.setY(EXIT_TEXT_Y)
				.setW(BUTTON_W)
				.setH(BUTTON_H)
				.setNormalTexture(A.getTexture(BUTTON_NORMAL))
				.setHoverTexture(A.getTexture(BUTTON_HOVER));

		gameOverText = new TextUI(MIDDLE, A.getFont(MINECRAFT_16))
				.setX(GAME_OVER_TEXT_X)
				.setY(GAME_OVER_TEXT_Y)
				.setTextAlign(TextUIAlign.MIDDLE);

		Gdx.input.setCursorCatched(true);

		/* Music */
		A.getMusic(MusicName.MAIN_MENU).stop();

		float volume = game.getSettings().getInteger(SETTINGS_MUSIC_VOLUME, SETTINGS_MUSIC_VOLUME_DEFAULT) / 100f;
		A.getMusic(MusicName.BOSS).setVolume(volume);
		A.getMusic(MusicName.BOSS).play();
	}

	@Override
	public void render(SpriteBatch batch) {
		/* Update */
		if (state == State.BOSS_FIGHT) {
			this.level = (int) ((1 - boss1.getHealth() / boss1.getMaxHealth()) * 100);
			this.time += Gdx.graphics.getRawDeltaTime();

			// All characters are dead
			if (tank.isDispose() && assassin.isDispose()) {
				gameOver();

				// Tank died
			} else if (character == tank && tank.isDispose()) {
				this.assassin.inflictDebuff(deadCharacterDebuff);
				switchCharacter(assassin);

				// Assassin died
			} else if (character == assassin && assassin.isDispose()) {
				this.tank.inflictDebuff(deadCharacterDebuff);
				switchCharacter(tank);

				// Boss is dead
			} else if (boss1.isDispose()) {
				gameOver();
			}
		}

		this.fpsText.setText(FPS_TEXT + Gdx.graphics.getFramesPerSecond());
		this.levelText.setText(LEVEL_TEXT + UntitledGame.formatLevel(level));
		this.timeText.setText(TIME_TEXT + UntitledGame.formatTime((int) time));
		this.scoreText.setText(SCORE_TEXT + score);

		/* Render Game */
		batch.setProjectionMatrix(cameraGame.combined);
		batch.begin();

		this.background.render(batch);
		this.floor.render(batch);
		this.entityManager.render(batch);
		this.floatingTextManager.render(batch);

		batch.end();

		/* Render UI */
		batch.setProjectionMatrix(cameraUI.combined);
		batch.begin();

		if (character == tank && !tank.isDispose()) {
			this.tankText.render(batch);
			this.tankHealthBar.render(batch);
			this.tankCooldowns.render(batch);

		} else if (character == assassin && !assassin.isDispose()) {
			this.assassinText.render(batch);
			this.assassinHealthBar.render(batch);
			this.assassinStackBar.render(batch);
			this.assassinCooldowns.render(batch);
		}

		if (!boss1.isDispose()) {
			this.bossText.render(batch);
			this.bossHealthBar.render(batch);
		}

		if (state == State.GAME_OVER_SCREEN) {
			batch.draw(A.getTexture(GAME_OVERLAY), 0, 0, GAME_WIDTH, GAME_HEIGHT);
			this.gameOverText.render(batch);
			this.exitButton.render(batch);
			this.exitText.render(batch);
		}

		this.fpsText.render(batch);
		this.levelText.render(batch);
		this.timeText.render(batch);
		this.scoreText.render(batch);
		this.highscoreText.render(batch);

		batch.end();
	}

	@Override
	public void renderDebug(ShapeRenderer renderer) {
		renderer.setProjectionMatrix(cameraGame.combined);
		renderer.begin();
		this.entityManager.renderDebugAll(renderer);
		renderer.end();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		this.viewportUI.update(width, height);
	}

	@Override
	public void pauseScreen() {
		A.getMusic(MusicName.BOSS).pause();
	}

	@Override
	public void resumeScreen() {
		A.getMusic(MusicName.BOSS).play();
	}

	public CharacterController getPlayerController() {
		return playerController;
	}

	private void gameOver() {
		state = State.GAME_OVER_TRANSITION;
		Gdx.input.setCursorCatched(false);
		multiplexer.addProcessor(exitButton);

		if (level > 0 && time > 0 && score > 0) {
			highscoreText.setText(HIGHSCORE_UPLOADING_TEXT);
			highscores.postHighscore(level, score, (int) time,
					() -> highscoreText.setText(HIGHSCORE_SUCCESS_TEXT),
					() -> highscoreText.setText(HIGHSCORE_FAILED_TEXT));
		}

		timer.scheduleTask(new Timer.Task() {
			@Override
			public void run() {
				state = State.GAME_OVER_SCREEN;

				if (level == 100) {
					gameOverText.setText(GAME_OVER_WIN_TEXT);
				} else {
					gameOverText.setText(GAME_OVER_LOSE_TEXT);
				}
			}
		}, GAME_OVER_TRANSITION_DURATION);
	}

	private void switchCharacter(Character next) {
		float x = character.getPosition().x;
		boolean flipX = character.getFlipX().get();
		this.character.getAlpha().set(0);

		this.character = next;
		this.character.endCrowdControl();
		this.character.getPosition().x = x;
		this.character.getPosition().y = GAME_FLOOR_HEIGHT;
		this.character.getFlipX().set(flipX);
		this.character.getAlpha().set(1);

		playerController.update();
//			Gdx.app.log("GameScreen.java", "Switched Characters");

		this.switchCharacter.begin();
		this.switchCharacter.run();
	}

	public void switchCharacter() {
		// Both characters are not dead
		if (!(tank.isDispose() && assassin.isDispose())) {
			boolean canSwitchCharacter = switchCharacter.get() == COOLDOWN_STATES;

			// Assassin is alive
			if (canSwitchCharacter && character == tank && !assassin.isDispose()) {
				switchCharacter(assassin);

				// Tank is alive
			} else if (canSwitchCharacter && character == assassin && !tank.isDispose()) {
				switchCharacter(tank);

				// Other character is not alive
			} else {
				character.endCrowdControl();
			}
		}
	}

	public void screenShake(int count, float offset, float interval) {
		if (count > 0) {
			timer.scheduleTask(new Timer.Task() {
				@Override
				public void run() {
					cameraGame.position.x = cameraX;
					cameraGame.position.x += offset;
					cameraGame.update();
					timer.scheduleTask(new Timer.Task() {
						@Override
						public void run() {
							cameraGame.position.x = cameraX;
							cameraGame.position.x -= offset;
							cameraGame.update();
							screenShake(count - 1, offset * 0.9f, interval);
						}
					}, interval);
				}
			}, interval);
		} else {
			timer.scheduleTask(new Timer.Task() {
				@Override
				public void run() {
					cameraGame.position.x = cameraX;
					cameraGame.update();
				}
			}, interval);
		}
	}

	public void addScore(int score) {
		this.score += score;
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
		return A;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public FloatingTextManager getFloatingTextManager() {
		return floatingTextManager;
	}
}
