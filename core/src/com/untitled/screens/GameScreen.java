package com.untitled.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.untitled.UntitledGame;
import com.untitled.assets.Assets;
import com.untitled.assets.FontName;
import com.untitled.assets.MusicName;
import com.untitled.assets.TextureName;
import com.untitled.game.EntityManager;
import com.untitled.game.FloatingTextManager;
import com.untitled.game.ability.CooldownState;
import com.untitled.game.boss1.Boss1;
import com.untitled.game.boss1.Boss1AI;
import com.untitled.game.boss1.Boss1Controller;
import com.untitled.game.character.Assassin;
import com.untitled.game.character.Character;
import com.untitled.game.character.CharacterController;
import com.untitled.game.character.Tank;
import com.untitled.game.debuff.Debuff;
import com.untitled.highscores.Highscores;
import com.untitled.ui.ButtonUI;
import com.untitled.ui.Cooldowns;
import com.untitled.ui.HealthBar;
import com.untitled.ui.StackBar;
import com.untitled.ui.TextUI;
import com.untitled.ui.TextUIAlign;
import com.untitled.ui.UIAlign;

import static com.untitled.game.debuff.DebuffType.DAMAGE_REDUCTION;

/**
 * Main game screen (Play) of Untitled
 */
public class GameScreen extends UntitledScreen {
	// Game Size
	public static final int GAME_WIDTH = UntitledGame.CAMERA_WIDTH;
	public static final int GAME_HEIGHT = UntitledGame.CAMERA_HEIGHT;
	public static final float GAME_FLOOR_HEIGHT = 66f;

	// Help
	private static final String TANK_TEXT = "TANK";
	private static final String ASSASSIN_TEXT = "ASSASSIN";

	private static final float CHARACTER_TEXT_X = 20f;
	private static final float CHARACTER_TEXT_Y = UntitledGame.CAMERA_HEIGHT - 20f;

	private static final String BLOCK = "[#42a5f5]BLOCK[]";
	private static final String HAMMER_SWING = "[#42a5f5]HAMMER SWING[]";
	private static final String FORTRESS = "[#42a5f5]FORTRESS[]";

	private static final String DASH = "[#42a5f5]DASH[]";
	private static final String SHURIKEN_THROW = "[#42a5f5]SHURIKEN THROW[]";
	private static final String CLEANSE = "[#42a5f5]CLEANSE[]";
	private static final String STACKS = "[#ffca28]STACKS[]";

	private static final String STUN = "[#ef5350]STUN[]";
	private static final String WEAK_SPOT = "[#ef5350]WEAK SPOT[]";

	private static final String TANK_HELP_TEXT = "[GOLD]TANK SKILLS[] - YOU MAY TRY THESE NOW." +
			"\n\n[[[GOLD]Q[]] KEY: " + BLOCK +
			"\n        The tank blocks some damage. If timed perfectly, he will take zero damage." +
			"\n        [#b2ebf2]If he blocks perfectly, " + HAMMER_SWING + " cooldown will be reset. The next " + HAMMER_SWING + " also deals bonus damage.[]" +
			"\n\n[[[GOLD]W[]] KEY: " + HAMMER_SWING +
			"\n        The tank swings his hammer to inflict some damage." +
			"\n        [#b2ebf2]If he attacks a " + WEAK_SPOT + " of the boss, the next " + FORTRESS + " will heal all characters.[]" +
			"\n\n[[[GOLD]E[]] KEY: " + FORTRESS +
			"\n        The tank blocks an attack similar to " + BLOCK + ", then gains some defensive buffs for awhile." +
			"\n        [#b2ebf2]If he blocks perfectly, the next " + BLOCK + " will also " + STUN + " the boss near him.[]" +
			"\n\n[[[GOLD]R[]] KEY: [#42a5f5]SWITCH CHARACTER[]" +
			"\n[[[GOLD]ARROW KEYS[]]: [#42a5f5]MOVEMENT[]";

	private static final float CHARACTER_HELP_COOLDOWNS_X = 20f;
	private static final float CHARACTER_HELP_COOLDOWNS_Y = CHARACTER_TEXT_Y - 16f;

	private static final String ASSASSIN_HELP_TEXT = "[GOLD]ASSASSIN SKILLS[] - YOU MAY TRY THESE NOW." +
			"\n\n[[[GOLD]Q[]] KEY: " + DASH +
			"\n        The assassin dashes in any direction, dodging all attacks." +
			"\n        [#b2ebf2]If the assassin dodges an attack, her " + STACKS + " increases. Dashing near a " + STUN + "[#ef5350]NED[] boss will deal true damage.[]" +
			"\n\n[[[GOLD]W[]] KEY: " + SHURIKEN_THROW +
			"\n        The assassin throws a shuriken to inflict some damage." +
			"\n        [#b2ebf2]At max " + STACKS + ", the shuriken will inflict bonus damage.[]" +
			"\n\n[[[GOLD]E[]] KEY: " + CLEANSE +
			"\n        The assassin removes any crowd control from herself and gains some offensive buffs for awhile." +
			"\n        [#b2ebf2]If she cleanses perfectly, the next " + SHURIKEN_THROW + " will inflict a " + WEAK_SPOT + " on the boss.[]" +
			"\n\n[[[GOLD]R[]] KEY: [#42a5f5]SWITCH CHARACTER[]" +
			"\n[[[GOLD]ARROW KEYS[]]: [#42a5f5]MOVEMENT[]";

	private static final float CHARACTER_HELP_TEXT_X = 50f;
	private static final float CHARACTER_HELP_TEXT_Y = UntitledGame.CAMERA_HEIGHT - 80f;

	private static final String START_BUTTON_TEXT = "START";
	private static final float START_BUTTON_X = UntitledGame.CAMERA_WIDTH - 100f;
	private static final float START_BUTTON_Y = 50f;

	// Game
	private static final float CHARACTER_HEALTH_BAR_X = CHARACTER_TEXT_X;
	private static final float CHARACTER_HEALTH_BAR_Y = CHARACTER_TEXT_Y - 10f;
	private static final float CHARACTER_HEALTH_BAR_W = UntitledGame.CAMERA_WIDTH / 2f;

	private static final float TANK_COOLDOWNS_X = CHARACTER_HEALTH_BAR_X;
	private static final float TANK_COOLDOWNS_Y = CHARACTER_HEALTH_BAR_Y - 16f;

	private static final float ASSASSIN_STACK_BAR_X = CHARACTER_HEALTH_BAR_X;
	private static final float ASSASSIN_STACK_BAR_Y = CHARACTER_HEALTH_BAR_Y - 10f;
	private static final float ASSASSIN_STACK_BAR_W = UntitledGame.CAMERA_WIDTH / 4f;

	private static final float ASSASSIN_COOLDOWNS_X = CHARACTER_HEALTH_BAR_X;
	private static final float ASSASSIN_COOLDOWNS_Y = ASSASSIN_STACK_BAR_Y - 16f;

	private static final float BOSS_BAR_X = 20f;
	private static final float BOSS_BAR_Y = 20f;
	private static final float BOSS_BAR_W = UntitledGame.CAMERA_WIDTH - 40f;

	private static final String BOSS_TEXT = "BOSS - GOLEM";
	private static final float BOSS_TEXT_X = 20f;
	private static final float BOSS_TEXT_Y = 32f;

	private static final String FPS_TEXT = "FPS: ";
	private static final float FPS_TEXT_X = UntitledGame.CAMERA_WIDTH - 5f;
	private static final float FPS_TEXT_Y = 5f;

	private static final String LEVEL_TEXT = "LEVEL: ";
	private static final float LEVEL_TEXT_X = UntitledGame.CAMERA_WIDTH - 20f;
	private static final float LEVEL_TEXT_Y = UntitledGame.CAMERA_HEIGHT - 20f;

	private static final String TIME_TEXT = "TIME: ";
	private static final float TIME_TEXT_X = UntitledGame.CAMERA_WIDTH - 20f;
	private static final float TIME_TEXT_Y = LEVEL_TEXT_Y - 14f;

	private static final String SCORE_TEXT = "SCORE: ";
	private static final float SCORE_TEXT_X = UntitledGame.CAMERA_WIDTH - 20f;
	private static final float SCORE_TEXT_Y = TIME_TEXT_Y - 14f;

	// Game Over
	private static final String HIGHSCORE_UPLOADING_TEXT = "UPLOADING HIGHSCORE...";
	private static final String HIGHSCORE_SUCCESS_TEXT = "UPLOAD HIGHSCORE SUCCESSFUL!";
	private static final String HIGHSCORE_FAILED_TEXT = "UPLOAD HIGHSCORE FAILED...";
	private static final float HIGHSCORE_TEXT_X = UntitledGame.CAMERA_WIDTH - 20f;
	private static final float HIGHSCORE_TEXT_Y = SCORE_TEXT_Y - 14f;

	private static final String GAME_OVER_LOSE_TEXT = "GAME OVER...";
	private static final String GAME_OVER_WIN_TEXT = "YOU DID IT!\n\nMORE BOSSES COMING SOON (TM)";
	private static final float GAME_OVER_TEXT_X = UntitledGame.CAMERA_WIDTH / 2f;
	private static final float GAME_OVER_TEXT_Y = UntitledGame.CAMERA_HEIGHT / 2f + 50f;

	private static final String EXIT_TEXT = "EXIT";
	private static final float EXIT_TEXT_X = UntitledGame.CAMERA_WIDTH / 2f;
	private static final float EXIT_TEXT_Y = UntitledGame.CAMERA_HEIGHT / 2f - 50f;

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
	private CharacterController characterController;
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

	/* Entities */
	private Character character;
	private Tank tank;
	private Assassin assassin;
	private Boss1 boss1;

	/* UI */
	private TextUI characterHelpText;
	private ButtonUI startButton;
	private TextUI startButtonText;

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
		HELP_SCREEN, BOSS_FIGHT, GAME_OVER_TRANSITION, GAME_OVER_SCREEN
	}

	public GameScreen(UntitledGame game) {
		super(game);

		this.A = game.getAssets();
		this.multiplexer = game.getInputMultiplexer();

		this.cameraGame = game.getCamera();
		this.cameraX = cameraGame.position.x;
		this.cameraUI = new OrthographicCamera();
		this.cameraUI.setToOrtho(false, UntitledGame.CAMERA_WIDTH, UntitledGame.CAMERA_HEIGHT);
		this.viewportUI = new FitViewport(UntitledGame.CAMERA_WIDTH, UntitledGame.CAMERA_HEIGHT, cameraUI);

		this.highscores = game.getHighscores();
		this.entityManager = new EntityManager();
		this.floatingTextManager = new FloatingTextManager(A);
		this.timer = new Timer();
		this.state = State.HELP_SCREEN;

		this.switchCharacter = new CooldownState(SWITCH_CHARACTER_COOLDOWN);
		this.deadCharacterDebuff = new Debuff(DAMAGE_REDUCTION, 1f, DEAD_CHARACTER_INVULNERABLE_DURATION);

		/* Entities */
		this.tank = new Tank(this);
		this.assassin = new Assassin(this);
		this.assassin.getAlpha().set(0);
		this.character = tank;

		/* Input */
		this.characterController = new CharacterController(this.character);

		/* UI */
		// Help
		this.characterHelpText = new TextUI(UIAlign.TOP_LEFT, A.getFont(FontName.MINECRAFT_8))
				.setX(CHARACTER_HELP_TEXT_X)
				.setY(CHARACTER_HELP_TEXT_Y)
				.setText(TANK_HELP_TEXT);

		this.startButton = new ButtonUI(UIAlign.MIDDLE, viewportUI, () -> {
			this.tank.dispose(0);
			this.assassin.dispose(0);
			this.entityManager = new EntityManager();

			// Character
			this.tank = new Tank(this);
			this.assassin = new Assassin(this);
			this.assassin.getAlpha().set(0);
			this.character = tank;
			this.characterController.setCharacter(character);

			tankHealthBar = new HealthBar(UIAlign.TOP_LEFT, tank, A.getTexture(TextureName.HEALTH_BAR_TANK), A.getTexture(TextureName.INFO_BAR_BACKGROUND))
					.setX(CHARACTER_HEALTH_BAR_X)
					.setY(CHARACTER_HEALTH_BAR_Y)
					.setW(CHARACTER_HEALTH_BAR_W);

			tankCooldowns = new Cooldowns(UIAlign.TOP_LEFT, A)
					.setX(TANK_COOLDOWNS_X)
					.setY(TANK_COOLDOWNS_Y)
					.add(tank.getBlockState(), A.getTexture(TextureName.COOLDOWN_BLOCK))
					.add(tank.getImpaleState(), A.getTexture(TextureName.COOLDOWN_HAMMER_SWING))
					.add(tank.getFortressState(), A.getTexture(TextureName.COOLDOWN_FORTRESS))
					.add(switchCharacter, A.getTexture(TextureName.COOLDOWN_SWITCH_CHARACTER));

			assassinHealthBar = new HealthBar(UIAlign.TOP_LEFT, assassin, A.getTexture(TextureName.HEALTH_BAR_ASSASSIN), A.getTexture(TextureName.INFO_BAR_BACKGROUND))
					.setX(CHARACTER_HEALTH_BAR_X)
					.setY(CHARACTER_HEALTH_BAR_Y)
					.setW(CHARACTER_HEALTH_BAR_W);

			assassinStackBar = new StackBar(UIAlign.TOP_LEFT, assassin, A.getTexture(TextureName.STACK_BAR_ASSASSIN), A.getTexture(TextureName.INFO_BAR_BACKGROUND))
					.setX(ASSASSIN_STACK_BAR_X)
					.setY(ASSASSIN_STACK_BAR_Y)
					.setW(ASSASSIN_STACK_BAR_W);

			assassinCooldowns = new Cooldowns(UIAlign.TOP_LEFT, A)
					.setX(ASSASSIN_COOLDOWNS_X)
					.setY(ASSASSIN_COOLDOWNS_Y)
					.add(assassin.getDashState(), A.getTexture(TextureName.COOLDOWN_DASH))
					.add(assassin.getShurikenState(), A.getTexture(TextureName.COOLDOWN_SHURIKEN_THROW))
					.add(assassin.getCleanseState(), A.getTexture(TextureName.COOLDOWN_CLEANSE))
					.add(switchCharacter, A.getTexture(TextureName.COOLDOWN_SWITCH_CHARACTER));

			// Boss
			this.boss1 = new Boss1(this);
			if (UntitledGame.DEBUG_BOSS_AI) {
				multiplexer.addProcessor(new Boss1Controller(this));
			} else {
				new Boss1AI(this);
			}

			bossHealthBar = new HealthBar(UIAlign.BOTTOM_LEFT, boss1, A.getTexture(TextureName.HEALTH_BAR_BOSS), A.getTexture(TextureName.INFO_BAR_BACKGROUND))
					.setX(BOSS_BAR_X)
					.setY(BOSS_BAR_Y)
					.setW(BOSS_BAR_W);

			// Music
			A.getMusic(MusicName.MAIN_MENU).stop();

			float volume = game.getSettings().getInteger(UntitledGame.SETTINGS_MUSIC_VOLUME, UntitledGame.SETTINGS_MUSIC_VOLUME_DEFAULT) / 100f;
			A.getMusic(MusicName.BOSS).setVolume(volume);
			A.getMusic(MusicName.BOSS).play();

			Gdx.input.setCursorCatched(true);
			multiplexer.removeProcessor(startButton);
			this.state = State.BOSS_FIGHT;
		})
				.setX(START_BUTTON_X)
				.setY(START_BUTTON_Y)
				.setW(UntitledGame.BUTTON_W)
				.setH(UntitledGame.BUTTON_H)
				.setNormalTexture(A.getTexture(TextureName.BUTTON_NORMAL))
				.setHoverTexture(A.getTexture(TextureName.BUTTON_HOVER));

		this.startButtonText = new TextUI(UIAlign.MIDDLE, A.getFont(FontName.MINECRAFT_8))
				.setX(START_BUTTON_X)
				.setY(START_BUTTON_Y)
				.setText(START_BUTTON_TEXT);

		// Tank
		tankText = new TextUI(UIAlign.TOP_LEFT, A.getFont(FontName.MINECRAFT_8))
				.setX(CHARACTER_TEXT_X)
				.setY(CHARACTER_TEXT_Y)
				.setText(TANK_TEXT);

		tankCooldowns = new Cooldowns(UIAlign.TOP_LEFT, A)
				.setX(CHARACTER_HELP_COOLDOWNS_X)
				.setY(CHARACTER_HELP_COOLDOWNS_Y)
				.add(tank.getBlockState(), A.getTexture(TextureName.COOLDOWN_BLOCK))
				.add(tank.getImpaleState(), A.getTexture(TextureName.COOLDOWN_HAMMER_SWING))
				.add(tank.getFortressState(), A.getTexture(TextureName.COOLDOWN_FORTRESS))
				.add(switchCharacter, A.getTexture(TextureName.COOLDOWN_SWITCH_CHARACTER));

		// Assassin
		assassinText = new TextUI(UIAlign.TOP_LEFT, A.getFont(FontName.MINECRAFT_8))
				.setX(CHARACTER_TEXT_X)
				.setY(CHARACTER_TEXT_Y)
				.setText(ASSASSIN_TEXT);

		assassinCooldowns = new Cooldowns(UIAlign.TOP_LEFT, A)
				.setX(CHARACTER_HELP_COOLDOWNS_X)
				.setY(CHARACTER_HELP_COOLDOWNS_Y)
				.add(assassin.getDashState(), A.getTexture(TextureName.COOLDOWN_DASH))
				.add(assassin.getShurikenState(), A.getTexture(TextureName.COOLDOWN_SHURIKEN_THROW))
				.add(assassin.getCleanseState(), A.getTexture(TextureName.COOLDOWN_CLEANSE))
				.add(switchCharacter, A.getTexture(TextureName.COOLDOWN_SWITCH_CHARACTER));

		// Boss
		bossText = new TextUI(UIAlign.BOTTOM_LEFT, A.getFont(FontName.MINECRAFT_8))
				.setX(BOSS_TEXT_X)
				.setY(BOSS_TEXT_Y)
				.setText(BOSS_TEXT);

		fpsText = new TextUI(UIAlign.BOTTOM_RIGHT, A.getFont(FontName.MINECRAFT_8))
				.setX(FPS_TEXT_X)
				.setY(FPS_TEXT_Y)
				.setColor(Color.GRAY);

		levelText = new TextUI(UIAlign.TOP_RIGHT, A.getFont(FontName.MINECRAFT_8))
				.setX(LEVEL_TEXT_X)
				.setY(LEVEL_TEXT_Y);

		timeText = new TextUI(UIAlign.TOP_RIGHT, A.getFont(FontName.MINECRAFT_8))
				.setX(TIME_TEXT_X)
				.setY(TIME_TEXT_Y);

		scoreText = new TextUI(UIAlign.TOP_RIGHT, A.getFont(FontName.MINECRAFT_8))
				.setX(SCORE_TEXT_X)
				.setY(SCORE_TEXT_Y);

		highscoreText = new TextUI(UIAlign.TOP_RIGHT, A.getFont(FontName.MINECRAFT_8))
				.setX(HIGHSCORE_TEXT_X)
				.setY(HIGHSCORE_TEXT_Y)
				.setColor(Color.GOLD);

		exitText = new TextUI(UIAlign.MIDDLE, A.getFont(FontName.MINECRAFT_8))
				.setX(EXIT_TEXT_X)
				.setY(EXIT_TEXT_Y)
				.setText(EXIT_TEXT);

		exitButton = new ButtonUI(UIAlign.MIDDLE, this.viewportUI, () -> setScreen(ScreenName.MAIN_MENU))
				.setX(EXIT_TEXT_X)
				.setY(EXIT_TEXT_Y)
				.setW(UntitledGame.BUTTON_W)
				.setH(UntitledGame.BUTTON_H)
				.setNormalTexture(A.getTexture(TextureName.BUTTON_NORMAL))
				.setHoverTexture(A.getTexture(TextureName.BUTTON_HOVER));

		gameOverText = new TextUI(UIAlign.MIDDLE, A.getFont(FontName.MINECRAFT_16))
				.setX(GAME_OVER_TEXT_X)
				.setY(GAME_OVER_TEXT_Y)
				.setTextAlign(TextUIAlign.MIDDLE);

		// Multiplexer
		multiplexer.addProcessor(this.characterController);
		multiplexer.addProcessor(this.startButton);
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

		batch.draw(A.getTexture(TextureName.GAME_BACKGROUND), 0, 0, UntitledGame.CAMERA_WIDTH, UntitledGame.CAMERA_HEIGHT);
		this.entityManager.render(batch);
		this.floatingTextManager.render(batch);

		batch.end();

		/* Render UI */
		batch.setProjectionMatrix(cameraUI.combined);
		batch.begin();

		if (state != State.HELP_SCREEN) {
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
		}

		switch (state) {
			case HELP_SCREEN:
				batch.draw(A.getTexture(TextureName.GAME_OVERLAY), 0, 0, GAME_WIDTH, GAME_HEIGHT);
				if (character == tank) {
					this.tankText.render(batch);
					this.tankCooldowns.render(batch);
				} else if (character == assassin) {
					this.assassinText.render(batch);
					this.assassinCooldowns.render(batch);
				}

				this.characterHelpText.render(batch);
				this.startButton.render(batch);
				this.startButtonText.render(batch);
				break;
			case GAME_OVER_SCREEN:
				batch.draw(A.getTexture(TextureName.GAME_OVERLAY), 0, 0, GAME_WIDTH, GAME_HEIGHT);
				this.gameOverText.render(batch);
				this.exitButton.render(batch);
				this.exitText.render(batch);
				break;
		}

		if (state != State.HELP_SCREEN) {
			this.fpsText.render(batch);
			this.levelText.render(batch);
			this.timeText.render(batch);
			this.scoreText.render(batch);
			this.highscoreText.render(batch);
		}

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
		if (state == State.HELP_SCREEN) {
			A.getMusic(MusicName.MAIN_MENU).pause();
		} else {
			A.getMusic(MusicName.BOSS).pause();
		}
	}

	@Override
	public void resumeScreen() {
		if (state == State.HELP_SCREEN) {
			A.getMusic(MusicName.MAIN_MENU).play();
		} else {
			A.getMusic(MusicName.BOSS).play();
		}
	}

	public CharacterController getCharacterController() {
		return characterController;
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

		boolean win = level == 100;
		if (!win) {
			multiplexer.removeProcessor(characterController);
		}

		timer.scheduleTask(new Timer.Task() {
			@Override
			public void run() {
				state = State.GAME_OVER_SCREEN;

				if (win) {
					gameOverText.setText(GAME_OVER_WIN_TEXT);
				} else {
					gameOverText.setText(GAME_OVER_LOSE_TEXT);
				}
			}
		}, GAME_OVER_TRANSITION_DURATION);
	}

	private void switchCharacter(Character next) {
		if (state == State.HELP_SCREEN) {
			if (next == tank) {
				characterHelpText.setText(TANK_HELP_TEXT);
			} else if (next == assassin) {
				characterHelpText.setText(ASSASSIN_HELP_TEXT);
			}
		}

		float x = character.getPosition().x;
		boolean flipX = character.getFlipX().get();
		this.character.getAlpha().set(0);

		this.character = next;
		this.character.endCrowdControl();
		this.character.getPosition().x = x;
		this.character.getPosition().y = GAME_FLOOR_HEIGHT;
		this.character.getFlipX().set(flipX);
		this.character.getAlpha().set(1);

		characterController.setCharacter(this.character);
//			Gdx.app.log("GameScreen.java", "Switched Characters");

		this.switchCharacter.begin();
		this.switchCharacter.run();
	}

	/**
	 * Switch the current character to the next character.
	 */
	public void switchCharacter() {
		// Both characters are not dead
		if (!(tank.isDispose() && assassin.isDispose())) {
			boolean canSwitchCharacter = switchCharacter.get() == CooldownState.COOLDOWN_STATES;

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

	/**
	 * Shake the game camera horizontally.
	 *
	 * @param count    the amount of times to shake the camera.
	 * @param offset   the distance to shake the camera.
	 * @param interval the interval between each shake.
	 */
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

	/**
	 * @param score adds this score to GameScreen.
	 */
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
