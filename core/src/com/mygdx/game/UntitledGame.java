package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.assets.AssassinAnimationName;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.assets.Boss1AnimationName;
import com.mygdx.game.assets.FontName;
import com.mygdx.game.assets.MusicName;
import com.mygdx.game.assets.RockAnimationName;
import com.mygdx.game.assets.ShurikenAnimationName;
import com.mygdx.game.assets.TankAnimationName;
import com.mygdx.game.highscores.Highscores;
import com.mygdx.game.screens.CreditsScreen;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.HighscoresScreen;
import com.mygdx.game.screens.MainMenuScreen;
import com.mygdx.game.screens.NameScreen;
import com.mygdx.game.screens.ScreenName;
import com.mygdx.game.screens.SettingsScreen;

import java.util.Locale;

import static com.mygdx.game.assets.TextureName.BACKGROUND;
import static com.mygdx.game.assets.TextureName.BUTTON_HOVER;
import static com.mygdx.game.assets.TextureName.BUTTON_MENU_HOVER;
import static com.mygdx.game.assets.TextureName.BUTTON_NORMAL;
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
import static com.mygdx.game.assets.TextureName.COOLDOWN_SWITCH_CHARACTER;
import static com.mygdx.game.assets.TextureName.FLOOR;
import static com.mygdx.game.assets.TextureName.HEALTH_BAR_ASSASSIN;
import static com.mygdx.game.assets.TextureName.HEALTH_BAR_BOSS;
import static com.mygdx.game.assets.TextureName.HEALTH_BAR_TANK;
import static com.mygdx.game.assets.TextureName.HIGHSCORES_EVEN;
import static com.mygdx.game.assets.TextureName.HIGHSCORES_ODD;
import static com.mygdx.game.assets.TextureName.HIGHSCORES_TITLE;
import static com.mygdx.game.assets.TextureName.INFO_BAR_BACKGROUND;
import static com.mygdx.game.assets.TextureName.STACK_BAR_ASSASSIN;
import static com.mygdx.game.assets.TextureName.STUNNED;
import static com.mygdx.game.assets.TextureName.WEAK_SPOT;
import static com.mygdx.game.screens.ScreenName.MAIN_MENU;
import static com.mygdx.game.screens.ScreenName.NAME_MENU;
import static com.mygdx.game.screens.ScreenName.SETTINGS;

public class UntitledGame extends Game {
	public static final String VERSION = "BETA 0.8";

	// Camera Size
	public static final int CAMERA_WIDTH = 640;
	public static final int CAMERA_HEIGHT = 360;

	// Window Size
	public static final int WINDOW_WIDTH = CAMERA_WIDTH * 2;
	public static final int WINDOW_HEIGHT = CAMERA_HEIGHT * 2;

	// Button Size
	public static final float BUTTON_W = 85f;
	public static final float BUTTON_H = 25f;

	// Game Variables
	public static final boolean DEBUG = false; // flag to view hitboxes
	public static final boolean BOSS1_AI = true; // flag to activate Boss1 AI.
	public static final float FLOOR_HEIGHT = 60;

	public static final String SETTINGS_NAME = "name";
	public static final String SETTINGS_VSYNC = "vsync";
	public static final boolean SETTINGS_VSYNC_DEFAULT = true;
	public static final String SETTINGS_FULLSCREEN = "fullscreen";
	public static final boolean SETTINGS_FULLSCREEN_DEFAULT = false;
	public static final String SETTINGS_MUSIC_VOLUME = "music volume";
	public static final int SETTINGS_MUSIC_VOLUME_DEFAULT = 30;
	public static final String SETTINGS_SOUND = "sound volume";
	public static final int SETTINGS_SOUND_DEFAULT = 50;
	private static final String PREFERENCES_SETTINGS = "settings";

	private Assets assets;
	private Highscores highscores;
	private InputMultiplexer inputMultiplexer;
	private OrthographicCamera camera;
	private Preferences settings;
	private SpriteBatch batch;
	private ShapeRenderer renderer;
	private Viewport viewport;

	@Override
	public void create() {
		batch = new SpriteBatch();
		renderer = new ShapeRenderer();
		renderer.setColor(Color.GOLD);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, CAMERA_WIDTH, CAMERA_HEIGHT);
		viewport = new FitViewport(CAMERA_WIDTH, CAMERA_HEIGHT, camera);
		inputMultiplexer = new InputMultiplexer();

		settings = Gdx.app.getPreferences(PREFERENCES_SETTINGS);

		assets = new Assets();

		// TODO: (Optimization) Move to Screens
		assets.loadTexture(BACKGROUND);
		assets.loadTexture(FLOOR);
		assets.loadTexture(STUNNED);
		assets.loadTexture(WEAK_SPOT);

		assets.loadTexture(BUTTON_NORMAL);
		assets.loadTexture(BUTTON_HOVER);
		assets.loadTexture(BUTTON_MENU_HOVER);

		assets.loadTexture(HIGHSCORES_TITLE);
		assets.loadTexture(HIGHSCORES_ODD);
		assets.loadTexture(HIGHSCORES_EVEN);

		assets.loadTexture(INFO_BAR_BACKGROUND);
		assets.loadTexture(HEALTH_BAR_TANK);
		assets.loadTexture(HEALTH_BAR_ASSASSIN);
		assets.loadTexture(STACK_BAR_ASSASSIN);
		assets.loadTexture(HEALTH_BAR_BOSS);

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
		assets.loadTexture(COOLDOWN_SWITCH_CHARACTER);

		assets.loadFont(FontName.MINECRAFT_8);
		assets.loadFont(FontName.MINECRAFT_16);
		assets.loadFont(FontName.MINECRAFT_24);
		assets.loadFont(FontName.MINECRAFT_32);

		assets.loadMusic(MusicName.MAIN_MENU);
		assets.loadMusic(MusicName.BOSS);

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
		Gdx.input.setInputProcessor(inputMultiplexer); // set processor for input.

		/* Settings */
		// Name
		String name = settings.getString(SETTINGS_NAME, null);

		// VSync
		Gdx.graphics.setVSync(settings.getBoolean(SETTINGS_VSYNC, SETTINGS_VSYNC_DEFAULT));

		// Fullscreen
		if (settings.getBoolean(SETTINGS_FULLSCREEN, SETTINGS_FULLSCREEN_DEFAULT)) {
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		} else {
			Gdx.graphics.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
		}

		if (name == null) {
			setScreen(NAME_MENU);
		} else {
			highscores = new Highscores(name);
			setScreen(MAIN_MENU);
		}
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

	public void setScreen(ScreenName screen) {
		inputMultiplexer.clear();
		switch (screen) {
			case NAME_MENU:
				setScreen(new NameScreen(this, MAIN_MENU));
				break;
			case NAME_SETTINGS:
				setScreen(new NameScreen(this, SETTINGS));
				break;
			case MAIN_MENU:
				setScreen(new MainMenuScreen(this));
				break;
			case GAME:
				setScreen(new GameScreen(this));
				break;
			case HIGHSCORES:
				setScreen(new HighscoresScreen(this));
				break;
			case SETTINGS:
				setScreen(new SettingsScreen(this));
				break;
			case CREDITS:
				setScreen(new CreditsScreen(this));
				break;
		}
	}

	public static String formatLevel(int level) {
		return String.format(Locale.US, "%.2f", level / 100f);
	}

	public static String formatTime(int time) {
		int s = time;
		int m = s / 60;
		int h = m / 24;

		s %= 60;
		m %= 60;
		if (h > 99) {
			return "OVER 9000!";
		} else {
			return String.format(Locale.US, "%02d:%02d:%02d", h, m, s);
		}
	}

	/* SETTERS */
	public void setName(String name) {
		settings.putString(SETTINGS_NAME, name);
		settings.flush();
		highscores = new Highscores(name);
	}

	/* GETTERS */
	public Assets getAssets() {
		return this.assets;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public Highscores getHighscores() {
		return highscores;
	}

	public InputMultiplexer getInputMultiplexer() {
		return this.inputMultiplexer;
	}

	public Preferences getSettings() {
		return settings;
	}

	public SpriteBatch getSpriteBatch() {
		return batch;
	}

	public ShapeRenderer getShapeRenderer() {
		return renderer;
	}

	public Viewport getViewport() {
		return viewport;
	}
}
