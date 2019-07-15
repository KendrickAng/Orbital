package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.assets.AssassinAnimationName;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.assets.Boss1AnimationName;
import com.mygdx.game.assets.RockAnimationName;
import com.mygdx.game.assets.ShurikenAnimationName;
import com.mygdx.game.assets.TankAnimationName;
import com.mygdx.game.screens.MainMenuScreen;

import static com.mygdx.game.assets.FontName.MINECRAFT_16;
import static com.mygdx.game.assets.FontName.MINECRAFT_24;
import static com.mygdx.game.assets.FontName.MINECRAFT_32;
import static com.mygdx.game.assets.FontName.MINECRAFT_8;
import static com.mygdx.game.assets.TextureName.BACKGROUND;
import static com.mygdx.game.assets.TextureName.BUTTON_HOVER;
import static com.mygdx.game.assets.TextureName.COOLDOWN_0;
import static com.mygdx.game.assets.TextureName.COOLDOWN_1;
import static com.mygdx.game.assets.TextureName.COOLDOWN_2;
import static com.mygdx.game.assets.TextureName.COOLDOWN_3;
import static com.mygdx.game.assets.TextureName.COOLDOWN_4;
import static com.mygdx.game.assets.TextureName.COOLDOWN_5;
import static com.mygdx.game.assets.TextureName.COOLDOWN_BLOCK;
import static com.mygdx.game.assets.TextureName.COOLDOWN_CLEANSE;
import static com.mygdx.game.assets.TextureName.COOLDOWN_DASH;
import static com.mygdx.game.assets.TextureName.COOLDOWN_FORTRESS;
import static com.mygdx.game.assets.TextureName.COOLDOWN_IMPALE;
import static com.mygdx.game.assets.TextureName.COOLDOWN_SHURIKEN_THROW;
import static com.mygdx.game.assets.TextureName.FLOOR;
import static com.mygdx.game.assets.TextureName.HEALTH_BAR_ASSASSIN;
import static com.mygdx.game.assets.TextureName.HEALTH_BAR_BACKGROUND;
import static com.mygdx.game.assets.TextureName.HEALTH_BAR_BOSS;
import static com.mygdx.game.assets.TextureName.HEALTH_BAR_TANK;
import static com.mygdx.game.assets.TextureName.STUNNED;
import static com.mygdx.game.assets.TextureName.WEAK_SPOT;

public class UntitledGame extends Game {
	// Window Size
	public static final int WINDOW_WIDTH = 640;
	public static final int WINDOW_HEIGHT = 360;

	// Menu Buttons
	public static final float BUTTON_WIDTH = WINDOW_WIDTH;
	public static final float BUTTON_HEIGHT = 40;

	// Game Variables
	public static final boolean DEBUG = false; // flag to view hitboxes
	public static final boolean BOSS1_AI = true; // flag to activate Boss1 AI.
	public static final float FLOOR_HEIGHT = 60;

	private Assets assets;
	private SpriteBatch batch;
	private ShapeRenderer renderer;
	private OrthographicCamera camera;
	private Viewport viewport;
	private InputMultiplexer inputMultiplexer;

	@Override
	public void create() {
		batch = new SpriteBatch();
		renderer = new ShapeRenderer();
		renderer.setColor(Color.GOLD);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);
		viewport = new FitViewport(WINDOW_WIDTH, WINDOW_HEIGHT, camera);
		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer); // set processor for input.

		assets = new Assets();

		// TODO: (Optimization) Move to Screens
		assets.loadTexture(BACKGROUND);
		assets.loadTexture(FLOOR);
		assets.loadTexture(STUNNED);
		assets.loadTexture(WEAK_SPOT);
		assets.loadTexture(BUTTON_HOVER);

		assets.loadTexture(HEALTH_BAR_ASSASSIN);
		assets.loadTexture(HEALTH_BAR_BACKGROUND);
		assets.loadTexture(HEALTH_BAR_BOSS);
		assets.loadTexture(HEALTH_BAR_TANK);

		assets.loadTexture(COOLDOWN_0);
		assets.loadTexture(COOLDOWN_1);
		assets.loadTexture(COOLDOWN_2);
		assets.loadTexture(COOLDOWN_3);
		assets.loadTexture(COOLDOWN_4);
		assets.loadTexture(COOLDOWN_5);
		assets.loadTexture(COOLDOWN_BLOCK);
		assets.loadTexture(COOLDOWN_IMPALE);
		assets.loadTexture(COOLDOWN_FORTRESS);
		assets.loadTexture(COOLDOWN_DASH);
		assets.loadTexture(COOLDOWN_SHURIKEN_THROW);
		assets.loadTexture(COOLDOWN_CLEANSE);

		assets.loadFont(MINECRAFT_8);
		assets.loadFont(MINECRAFT_16);
		assets.loadFont(MINECRAFT_24);
		assets.loadFont(MINECRAFT_32);

		assets.loadTankAnimation(TankAnimationName.STANDING);
		assets.loadTankAnimation(TankAnimationName.WALKING);
		assets.loadTankAnimation(TankAnimationName.BLOCK);
		assets.loadTankAnimation(TankAnimationName.IMPALE);
		assets.loadTankAnimation(TankAnimationName.FORTRESS);
		assets.loadTankAnimation(TankAnimationName.FORTRESS_STANDING);
		assets.loadTankAnimation(TankAnimationName.FORTRESS_WALKING);
		assets.loadTankAnimation(TankAnimationName.FORTRESS_BLOCK);
		assets.loadTankAnimation(TankAnimationName.FORTRESS_IMPALE);

		assets.loadAssassinAnimation(AssassinAnimationName.STANDING);
		assets.loadAssassinAnimation(AssassinAnimationName.WALKING);
		assets.loadAssassinAnimation(AssassinAnimationName.DASH);
		assets.loadAssassinAnimation(AssassinAnimationName.SHURIKEN_THROW);

		assets.loadBoss1Animation(Boss1AnimationName.STANDING);
		assets.loadBoss1Animation(Boss1AnimationName.GROUND_SMASH);
		assets.loadBoss1Animation(Boss1AnimationName.EARTHQUAKE);
		assets.loadBoss1Animation(Boss1AnimationName.ROLL);

		assets.loadShurikenAnimation(ShurikenAnimationName.FLYING);
		assets.loadRockAnimation(RockAnimationName.ERUPT);
		assets.load();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
		renderer.dispose();
	}

	/* GETTERS */
	public SpriteBatch getSpriteBatch() {
		return batch;
	}

	public ShapeRenderer getShapeRenderer() {
		return renderer;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public Viewport getViewport() {
		return viewport;
	}

	public InputMultiplexer getInputMultiplexer() {
		return this.inputMultiplexer;
	}

	public Assets getAssets() {
		return this.assets;
	}
}
