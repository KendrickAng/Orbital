package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
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

import static com.mygdx.game.UntitledGame.BOSS1_AI;
import static com.mygdx.game.UntitledGame.CAMERA_HEIGHT;
import static com.mygdx.game.UntitledGame.CAMERA_WIDTH;
import static com.mygdx.game.UntitledGame.FLOOR_HEIGHT;
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

	private static final String SCORE_TEXT = "SCORE: ";
	private static final float SCORE_TEXT_X = CAMERA_WIDTH - 20f;
	private static final float SCORE_TEXT_Y = CAMERA_HEIGHT - 20f;

	private static final String TIME_TEXT = "TIME: ";
	private static final float TIME_TEXT_X = CAMERA_WIDTH - 20f;
	private static final float TIME_TEXT_Y = SCORE_TEXT_Y - 14f;

	private static final String CONTINUE_TEXT = "CONTINUE";
	private static final float CONTINUE_TEXT_X = CAMERA_WIDTH / 2f;
	private static final float CONTINUE_TEXT_Y = CAMERA_HEIGHT / 2f;
	private static final float CONTINUE_W = 160;
	private static final float CONTINUE_H = 40;

	private static final float SWITCH_CHARACTER_COOLDOWN = 2f;
	private static final float DEAD_CHARACTER_INVULNERABLE_DURATION = 1f;

	private Assets A;
	private Highscores highscores;

	private Background background;
	private Floor floor;

	private CharacterController playerController;
	private EntityManager entityManager;
	private FloatingTextManager floatingTextManager;

	private int score;
	private int level;
	private float time;
	private boolean gameOver;

	private CooldownState switchCharacter;
	private Debuff deadCharacterDebuff;

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
	private TextUI scoreText;
	private TextUI timeText;

	private TextUI continueText;
	private ButtonUI continueButton;

	public GameScreen(UntitledGame game) {
		super(game);

		Viewport viewport = game.getViewport();
		InputMultiplexer multiplexer = game.getInputMultiplexer();

		this.A = game.getAssets();
		this.highscores = game.getHighscores();
		this.entityManager = new EntityManager();
		this.floatingTextManager = new FloatingTextManager(A);

		this.switchCharacter = new CooldownState(SWITCH_CHARACTER_COOLDOWN);
		this.deadCharacterDebuff = new Debuff(DAMAGE_REDUCTION, 1f, DEAD_CHARACTER_INVULNERABLE_DURATION);

		/* Entities */
		this.tank = new Tank(this);
		this.assassin = new Assassin(this);
		this.assassin.setVisible(false);
		this.character = tank;
		this.boss1 = new Boss1(this);

		/* Input */
		this.playerController = new CharacterController(this);

		multiplexer.addProcessor(playerController);
		if (BOSS1_AI) {
			new Boss1AI(this);
		} else {
			multiplexer.addProcessor(new Boss1Controller(this));
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

		scoreText = new TextUI(TOP_RIGHT, A.getFont(MINECRAFT_8))
				.setX(SCORE_TEXT_X)
				.setY(SCORE_TEXT_Y);

		timeText = new TextUI(TOP_RIGHT, A.getFont(MINECRAFT_8))
				.setX(TIME_TEXT_X)
				.setY(TIME_TEXT_Y);

		continueText = new TextUI(MIDDLE, A.getFont(MINECRAFT_16))
				.setX(CONTINUE_TEXT_X)
				.setY(CONTINUE_TEXT_Y)
				.setText(CONTINUE_TEXT);

		continueButton = new ButtonUI(MIDDLE, viewport, () -> {
			if (gameOver) {
				setScreen(MAIN_MENU);
			}
		})
				.setX(CONTINUE_TEXT_X)
				.setY(CONTINUE_TEXT_Y)
				.setW(CONTINUE_W)
				.setH(CONTINUE_H)
				.setNormalTexture(A.getTexture(BUTTON_NORMAL))
				.setHoverTexture(A.getTexture(BUTTON_HOVER));

		multiplexer.addProcessor(continueButton);

		/* Music */
		A.getMusic(MusicName.MAIN_MENU).stop();

		float volume = game.getSettings().getInteger(SETTINGS_MUSIC_VOLUME, SETTINGS_MUSIC_VOLUME_DEFAULT) / 100f;
		A.getMusic(MusicName.GAME).setVolume(volume);
		A.getMusic(MusicName.GAME).play();
	}

	@Override
	public void update() {
		if (!gameOver) {
			time += Gdx.graphics.getRawDeltaTime();

			// All characters are dead
			if (tank.isDispose() && assassin.isDispose()) {
				gameOver();

				// Tank died
			} else if (character == tank && tank.isDispose()) {
				assassin.inflictDebuff(deadCharacterDebuff);
				switchCharacter();

				// Assassin died
			} else if (character == assassin && assassin.isDispose()) {
				tank.inflictDebuff(deadCharacterDebuff);
				switchCharacter();

				// Boss is dead
			} else if (boss1.isDispose()) {
				level++;
				gameOver();
			}
		}

		fpsText.setText(FPS_TEXT + Gdx.graphics.getFramesPerSecond());
		scoreText.setText(SCORE_TEXT + score);
		timeText.setText(TIME_TEXT + UntitledGame.formatTime((int) time));
	}

	@Override
	public void render(SpriteBatch batch) {
		background.render(batch);
		floor.render(batch);
		entityManager.render(batch);
		floatingTextManager.render(batch);

		// UI
		if (character == tank && !tank.isDispose()) {
			tankText.render(batch);
			tankHealthBar.render(batch);
			tankCooldowns.render(batch);

		} else if (character == assassin && !assassin.isDispose()) {
			assassinText.render(batch);
			assassinHealthBar.render(batch);
			assassinStackBar.render(batch);
			assassinCooldowns.render(batch);
		}

		if (!boss1.isDispose()) {
			bossText.render(batch);
			bossHealthBar.render(batch);
		}

		fpsText.render(batch);
		scoreText.render(batch);
		timeText.render(batch);

		if (gameOver) {
			continueButton.render(batch);
			continueText.render(batch);
		}
	}

	@Override
	public void renderDebug(ShapeRenderer renderer) {
		entityManager.renderDebugAll(renderer);
	}

	public CharacterController getPlayerController() {
		return playerController;
	}

	private void gameOver() {
		gameOver = true;
		highscores.postHighscore(level, score, (int) time);
	}

	public void switchCharacter() {
		if (!gameOver) {
			boolean canSwitchCharacter = switchCharacter.get() == COOLDOWN_STATES;
			Character next;

			// Assassin is alive
			if (canSwitchCharacter && character == tank && !assassin.isDispose()) {
				next = assassin;

				// Tank is alive
			} else if (canSwitchCharacter && character == assassin && !tank.isDispose()) {
				next = tank;

				// Other character is not alive
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
			character.getPosition().y = FLOOR_HEIGHT;
			character.getFlipX().set(flipX);

			playerController.update();
//			Gdx.app.log("GameScreen.java", "Switched Characters");

			this.switchCharacter.begin();
			this.switchCharacter.run();
		}
	}

	public void addScore(int score) {
		this.score += score;
	}

	/* Getters */
	public boolean isGameOver() {
		return gameOver;
	}

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
