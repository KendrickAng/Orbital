package com.mygdx.game.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Background;
import com.mygdx.game.Floor;
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
import com.mygdx.game.ui.TextUI;

import static com.mygdx.game.UntitledGame.BOSS1_AI;
import static com.mygdx.game.UntitledGame.FLOOR_HEIGHT;
import static com.mygdx.game.UntitledGame.WINDOW_HEIGHT;
import static com.mygdx.game.UntitledGame.WINDOW_WIDTH;
import static com.mygdx.game.assets.FontName.MINECRAFT_8;
import static com.mygdx.game.assets.TextureName.COOLDOWN_BLOCK;
import static com.mygdx.game.assets.TextureName.COOLDOWN_CLEANSE;
import static com.mygdx.game.assets.TextureName.COOLDOWN_DASH;
import static com.mygdx.game.assets.TextureName.COOLDOWN_FORTRESS;
import static com.mygdx.game.assets.TextureName.COOLDOWN_IMPALE;
import static com.mygdx.game.assets.TextureName.COOLDOWN_SHURIKEN_THROW;
import static com.mygdx.game.assets.TextureName.HEALTH_BAR_ASSASSIN;
import static com.mygdx.game.assets.TextureName.HEALTH_BAR_BACKGROUND;
import static com.mygdx.game.assets.TextureName.HEALTH_BAR_BOSS;
import static com.mygdx.game.assets.TextureName.HEALTH_BAR_TANK;
import static com.mygdx.game.ui.UIAlign.BOTTOM_LEFT;
import static com.mygdx.game.ui.UIAlign.TOP_LEFT;
import static com.mygdx.game.ui.UIAlign.TOP_RIGHT;

public class GameScreen extends UntitledScreen {
	private static final String TANK_TEXT = "TANK";
	private static final String ASSASSIN_TEXT = "ASSASSIN";

	private static final float CHARACTER_TEXT_X = 20f;
	private static final float CHARACTER_TEXT_Y = WINDOW_HEIGHT - 20f;

	private static final float CHARACTER_BAR_X = CHARACTER_TEXT_X;
	private static final float CHARACTER_BAR_Y = CHARACTER_TEXT_Y - 10f;
	private static final float CHARACTER_BAR_W = WINDOW_WIDTH / 2f;

	private static final float COOLDOWNS_X = CHARACTER_BAR_X;
	private static final float COOLDOWNS_Y = CHARACTER_BAR_Y - 16f;

	private static final float BOSS_BAR_X = 20f;
	private static final float BOSS_BAR_Y = 20f;
	private static final float BOSS_BAR_W = WINDOW_WIDTH - 40f;

	private static final String BOSS_TEXT = "BOSS - GOLEM";
	private static final float BOSS_TEXT_X = 20f;
	private static final float BOSS_TEXT_Y = 32f;

	private static final String SCORE_TEXT = "SCORE: ";
	private static final float SCORE_TEXT_X = WINDOW_WIDTH - 20f;
	private static final float SCORE_TEXT_Y = WINDOW_HEIGHT - 20f;

	private Assets A;

	private Background background;
	private Floor floor;

	private CharacterController playerController;
	private EntityManager entityManager;

	/* Entities */
	private Character character;
	private Tank tank;
	private Assassin assassin;
	private Boss1 boss1;

	/* UI */
	private TextUI tankText;
	private TextUI assassinText;
	private HealthBar tankBar;
	private HealthBar assassinBar;
	private Cooldowns tankCooldowns;
	private Cooldowns assassinCooldowns;

	private TextUI bossText;
	private HealthBar bossBar;

	private TextUI scoreText;

	public GameScreen(UntitledGame game) {
		super(game);

		this.A = game.getAssets();
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

		this.background = new Background(A);
		this.floor = new Floor(A);

		/* Health Bars */
		tankText = new TextUI(TOP_LEFT, A.getFont(MINECRAFT_8))
				.setX(CHARACTER_TEXT_X)
				.setY(CHARACTER_TEXT_Y)
				.setText(TANK_TEXT);

		assassinText = new TextUI(TOP_LEFT, A.getFont(MINECRAFT_8))
				.setX(CHARACTER_TEXT_X)
				.setY(CHARACTER_TEXT_Y)
				.setText(ASSASSIN_TEXT);

		tankBar = new HealthBar(TOP_LEFT, tank, A.getTexture(HEALTH_BAR_TANK), A.getTexture(HEALTH_BAR_BACKGROUND))
				.setX(CHARACTER_BAR_X)
				.setY(CHARACTER_BAR_Y)
				.setW(CHARACTER_BAR_W);

		assassinBar = new HealthBar(TOP_LEFT, assassin, A.getTexture(HEALTH_BAR_ASSASSIN), A.getTexture(HEALTH_BAR_BACKGROUND))
				.setX(CHARACTER_BAR_X)
				.setY(CHARACTER_BAR_Y)
				.setW(CHARACTER_BAR_W);

		tankCooldowns = new Cooldowns(TOP_LEFT, A)
				.setX(COOLDOWNS_X)
				.setY(COOLDOWNS_Y)
				.add(tank.getBlockState(), A.getTexture(COOLDOWN_BLOCK))
				.add(tank.getImpaleState(), A.getTexture(COOLDOWN_IMPALE))
				.add(tank.getFortressState(), A.getTexture(COOLDOWN_FORTRESS));

		assassinCooldowns = new Cooldowns(TOP_LEFT, A)
				.setX(COOLDOWNS_X)
				.setY(COOLDOWNS_Y)
				.add(assassin.getDashState(), A.getTexture(COOLDOWN_DASH))
				.add(assassin.getShurikenState(), A.getTexture(COOLDOWN_SHURIKEN_THROW))
				.add(assassin.getCleanseState(), A.getTexture(COOLDOWN_CLEANSE));

		bossText = new TextUI(BOTTOM_LEFT, A.getFont(MINECRAFT_8))
				.setX(BOSS_TEXT_X)
				.setY(BOSS_TEXT_Y)
				.setText(BOSS_TEXT);

		bossBar = new HealthBar(BOTTOM_LEFT, boss1, A.getTexture(HEALTH_BAR_BOSS), A.getTexture(HEALTH_BAR_BACKGROUND))
				.setX(BOSS_BAR_X)
				.setY(BOSS_BAR_Y)
				.setW(BOSS_BAR_W);

		scoreText = new TextUI(TOP_RIGHT, A.getFont(MINECRAFT_8))
				.setX(SCORE_TEXT_X)
				.setY(SCORE_TEXT_Y);
	}

	@Override
	public void update() {
		// TODO: Abstract these
		if (character == tank && tank.isDispose()) {
			switchCharacter();
		}

		if (character == assassin && assassin.isDispose()) {
			switchCharacter();
		}

		int score = (int) (boss1.getMaxHealth() - boss1.getHealth());
		scoreText.setText(SCORE_TEXT + score);
	}

	@Override
	public void render(SpriteBatch batch) {
		background.render(batch);
		floor.render(batch);
		entityManager.renderAll(batch);

		if (character == tank && !tank.isDispose()) {
			tankText.render(batch);
			tankBar.render(batch);
			tankCooldowns.render(batch);
		} else if (character == assassin && !assassin.isDispose()) {
			assassinText.render(batch);
			assassinBar.render(batch);
			assassinCooldowns.render(batch);
		}

		if (!boss1.isDispose()) {
			bossText.render(batch);
			bossBar.render(batch);
		}

		scoreText.render(batch);
	}

	@Override
	public void renderDebug(ShapeRenderer renderer) {
		entityManager.renderDebugAll(renderer);
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
			character.getPosition().y = FLOOR_HEIGHT;
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
		return A;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}
}
