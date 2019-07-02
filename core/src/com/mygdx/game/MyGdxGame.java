package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.screens.MainMenuScreen;
import com.mygdx.game.shape.Point;
import com.mygdx.game.ui.ColorTextures;

import static com.mygdx.game.assets.Assets.TextureName.BACKGROUND;
import static com.mygdx.game.assets.Assets.TextureName.FLOOR;
import static com.mygdx.game.assets.Assets.TextureName.HEALTH_BAR_ASSASSIN;
import static com.mygdx.game.assets.Assets.TextureName.HEALTH_BAR_BACKGROUND;
import static com.mygdx.game.assets.Assets.TextureName.HEALTH_BAR_BOSS;
import static com.mygdx.game.assets.Assets.TextureName.HEALTH_BAR_TANK;

public class MyGdxGame extends Game {
	// define game variables
	public static final int GAME_WIDTH = 600;
	public static final int GAME_HEIGHT = 380;
	public static final int MAP_HEIGHT = GAME_HEIGHT / 9; // accounts for ground

	public static final boolean DEBUG = true; // flag to view hitboxes
	public static final boolean BOSS1_AI = true; // flag to activate Boss1 AI.

	private ColorTextures colorTextures;

	private Assets assets;
	private SpriteBatch batch;
	private BitmapFont font;
	private OrthographicCamera camera;
	private Viewport viewport;
	private InputMultiplexer inputMultiplexer;

	@Override
	public void create() {
		colorTextures = new ColorTextures();
		batch = new SpriteBatch();
		font = new BitmapFont();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);
		viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera);
		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer); // set processor for input.

		assets = new Assets();
		assets.loadTexture(BACKGROUND);
		assets.loadTexture(FLOOR);
		assets.loadTexture(HEALTH_BAR_ASSASSIN);
		assets.loadTexture(HEALTH_BAR_BACKGROUND);
		assets.loadTexture(HEALTH_BAR_BOSS);
		assets.loadTexture(HEALTH_BAR_TANK);

		assets.loadTankAnimation(Assets.TankAnimationName.STANDING);
		assets.loadTankAnimation(Assets.TankAnimationName.WALKING);
		assets.loadTankAnimation(Assets.TankAnimationName.BLOCK);
		assets.loadTankAnimation(Assets.TankAnimationName.IMPALE);

		assets.loadAssassinAnimation(Assets.AssassinAnimationName.STANDING);
		assets.loadAssassinAnimation(Assets.AssassinAnimationName.WALKING);
		assets.loadAssassinAnimation(Assets.AssassinAnimationName.DASH);
		assets.loadAssassinAnimation(Assets.AssassinAnimationName.SHURIKEN_THROW);

		assets.loadBoss1Animation(Assets.Boss1AnimationName.STANDING);
		assets.loadBoss1Animation(Assets.Boss1AnimationName.GROUND_SMASH);
		assets.loadBoss1Animation(Assets.Boss1AnimationName.EARTHQUAKE);
		assets.loadBoss1Animation(Assets.Boss1AnimationName.ROLL);

		assets.loadShurikenAnimation(Assets.ShurikenAnimationName.FLYING);
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
		font.dispose();
		colorTextures.dispose();
	}

	public Point unproject(float x, float y) {
		Vector2 vector2 = new Vector2(x, y);
		viewport.unproject(vector2);
		return new Point(vector2.x, vector2.y);
	}

	/* GETTERS */
	public ColorTextures getColorTextures() {
		return this.colorTextures;
	}

	public SpriteBatch getSpriteBatch() {
		return batch;
	}

	public BitmapFont getFont() {
		return font;
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
