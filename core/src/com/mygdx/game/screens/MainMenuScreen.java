package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.UntitledGame;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.assets.MusicName;
import com.mygdx.game.screens.game.Background;
import com.mygdx.game.ui.ButtonUI;
import com.mygdx.game.ui.TextUI;

import static com.mygdx.game.UntitledGame.CAMERA_HEIGHT;
import static com.mygdx.game.UntitledGame.CAMERA_WIDTH;
import static com.mygdx.game.UntitledGame.SETTINGS_MUSIC_VOLUME;
import static com.mygdx.game.UntitledGame.SETTINGS_MUSIC_VOLUME_DEFAULT;
import static com.mygdx.game.UntitledGame.VERSION;
import static com.mygdx.game.assets.FontName.MINECRAFT_32;
import static com.mygdx.game.assets.FontName.MINECRAFT_8;
import static com.mygdx.game.assets.MusicName.MAIN_MENU;
import static com.mygdx.game.assets.TextureName.BUTTON_MENU_HOVER;
import static com.mygdx.game.screens.ScreenName.CREDITS;
import static com.mygdx.game.screens.ScreenName.GAME;
import static com.mygdx.game.screens.ScreenName.HIGHSCORES;
import static com.mygdx.game.screens.ScreenName.SETTINGS;
import static com.mygdx.game.ui.UIAlign.BOTTOM_RIGHT;
import static com.mygdx.game.ui.UIAlign.MIDDLE;
import static com.mygdx.game.ui.UIAlign.TOP_MIDDLE;

public class MainMenuScreen extends UntitledScreen {
	private static final String TITLE = "UNTITLED";
	private static final float TITLE_X = CAMERA_WIDTH / 2f;
	private static final float TITLE_Y = CAMERA_HEIGHT - 50f;

	// Menu Buttons
	private static final float BUTTON_MENU_WIDTH = CAMERA_WIDTH;
	private static final float BUTTON_MENU_HEIGHT = 30;

	private static final String PLAY_BUTTON_TEXT = "PLAY";
	private static final float PLAY_BUTTON_X = CAMERA_WIDTH / 2f;
	private static final float PLAY_BUTTON_Y = TITLE_Y - 120f;

	private static final String HIGHSCORES_BUTTON_TEXT = "HIGHSCORES";
	private static final float HIGHSCORES_BUTTON_X = CAMERA_WIDTH / 2f;
	private static final float HIGHSCORES_BUTTON_Y = PLAY_BUTTON_Y - BUTTON_MENU_HEIGHT;

	private static final String SETTINGS_BUTTON_TEXT = "SETTINGS";
	private static final float SETTINGS_BUTTON_X = CAMERA_WIDTH / 2f;
	private static final float SETTINGS_BUTTON_Y = HIGHSCORES_BUTTON_Y - BUTTON_MENU_HEIGHT;

	private static final String CREDITS_BUTTON_TEXT = "CREDITS";
	private static final float CREDITS_BUTTON_X = CAMERA_WIDTH / 2f;
	private static final float CREDITS_BUTTON_Y = SETTINGS_BUTTON_Y - BUTTON_MENU_HEIGHT;

	private static final String EXIT_BUTTON_TEXT = "EXIT";
	private static final float EXIT_BUTTON_X = CAMERA_WIDTH / 2f;
	private static final float EXIT_BUTTON_Y = CREDITS_BUTTON_Y - BUTTON_MENU_HEIGHT;

	private static final float VERSION_TEXT_X = CAMERA_WIDTH - 10f;
	private static final float VERSION_TEXT_Y = 10f;

	private Assets A;
	private Background background;

	private TextUI title;

	private ButtonUI playButton;
	private TextUI playButtonText;

	private ButtonUI highscoresButton;
	private TextUI highscoresButtonText;

	private ButtonUI settingsButton;
	private TextUI settingsButtonText;

	private ButtonUI creditsButton;
	private TextUI creditsButtonText;

	private ButtonUI exitButton;
	private TextUI exitButtonText;

	private TextUI versionText;

	public MainMenuScreen(UntitledGame game) {
		super(game);
		Viewport viewport = game.getViewport();
		InputMultiplexer multiplexer = game.getInputMultiplexer();

		this.A = game.getAssets();
		this.background = new Background(A);

		/* UI */
		this.title = new TextUI(TOP_MIDDLE, A.getFont(MINECRAFT_32))
				.setX(TITLE_X)
				.setY(TITLE_Y)
				.setText(TITLE);

		this.playButton = new ButtonUI(MIDDLE, viewport, () -> setScreen(GAME))
				.setX(PLAY_BUTTON_X)
				.setY(PLAY_BUTTON_Y)
				.setW(BUTTON_MENU_WIDTH)
				.setH(BUTTON_MENU_HEIGHT)
				.setHoverTexture(A.getTexture(BUTTON_MENU_HOVER));

		this.playButtonText = new TextUI(MIDDLE, A.getFont(MINECRAFT_8))
				.setX(PLAY_BUTTON_X)
				.setY(PLAY_BUTTON_Y)
				.setText(PLAY_BUTTON_TEXT);

		this.highscoresButton = new ButtonUI(MIDDLE, viewport, () -> setScreen(HIGHSCORES))
				.setX(HIGHSCORES_BUTTON_X)
				.setY(HIGHSCORES_BUTTON_Y)
				.setW(BUTTON_MENU_WIDTH)
				.setH(BUTTON_MENU_HEIGHT)
				.setHoverTexture(A.getTexture(BUTTON_MENU_HOVER));

		this.highscoresButtonText = new TextUI(MIDDLE, A.getFont(MINECRAFT_8))
				.setX(HIGHSCORES_BUTTON_X)
				.setY(HIGHSCORES_BUTTON_Y)
				.setText(HIGHSCORES_BUTTON_TEXT);

		this.settingsButton = new ButtonUI(MIDDLE, viewport, () -> setScreen(SETTINGS))
				.setX(SETTINGS_BUTTON_X)
				.setY(SETTINGS_BUTTON_Y)
				.setW(BUTTON_MENU_WIDTH)
				.setH(BUTTON_MENU_HEIGHT)
				.setHoverTexture(A.getTexture(BUTTON_MENU_HOVER));

		this.settingsButtonText = new TextUI(MIDDLE, A.getFont(MINECRAFT_8))
				.setX(SETTINGS_BUTTON_X)
				.setY(SETTINGS_BUTTON_Y)
				.setText(SETTINGS_BUTTON_TEXT);

		this.creditsButton = new ButtonUI(MIDDLE, viewport, () -> setScreen(CREDITS))
				.setX(CREDITS_BUTTON_X)
				.setY(CREDITS_BUTTON_Y)
				.setW(BUTTON_MENU_WIDTH)
				.setH(BUTTON_MENU_HEIGHT)
				.setHoverTexture(A.getTexture(BUTTON_MENU_HOVER));

		this.creditsButtonText = new TextUI(MIDDLE, A.getFont(MINECRAFT_8))
				.setX(CREDITS_BUTTON_X)
				.setY(CREDITS_BUTTON_Y)
				.setText(CREDITS_BUTTON_TEXT);

		this.exitButton = new ButtonUI(MIDDLE, viewport, () -> Gdx.app.exit())
				.setX(EXIT_BUTTON_X)
				.setY(EXIT_BUTTON_Y)
				.setW(BUTTON_MENU_WIDTH)
				.setH(BUTTON_MENU_HEIGHT)
				.setHoverTexture(A.getTexture(BUTTON_MENU_HOVER));

		this.exitButtonText = new TextUI(MIDDLE, A.getFont(MINECRAFT_8))
				.setX(EXIT_BUTTON_X)
				.setY(EXIT_BUTTON_Y)
				.setText(EXIT_BUTTON_TEXT);

		this.versionText = new TextUI(BOTTOM_RIGHT, A.getFont(MINECRAFT_8))
				.setX(VERSION_TEXT_X)
				.setY(VERSION_TEXT_Y)
				.setText(VERSION);

		// Add input processors
		multiplexer.addProcessor(playButton);
		multiplexer.addProcessor(highscoresButton);
		multiplexer.addProcessor(settingsButton);
		multiplexer.addProcessor(creditsButton);
		multiplexer.addProcessor(exitButton);

		/* Music */
		A.getMusic(MusicName.GAME).stop();

		float volume = game.getSettings().getInteger(SETTINGS_MUSIC_VOLUME, SETTINGS_MUSIC_VOLUME_DEFAULT) / 100f;
		A.getMusic(MAIN_MENU).setVolume(volume);
		A.getMusic(MAIN_MENU).play();

	}

	@Override
	public void update() {

	}

	@Override
	public void render(SpriteBatch batch) {
		this.background.render(batch);
		this.title.render(batch);

		this.playButton.render(batch);
		this.playButtonText.render(batch);

		this.highscoresButton.render(batch);
		this.highscoresButtonText.render(batch);

		this.settingsButton.render(batch);
		this.settingsButtonText.render(batch);

		this.creditsButton.render(batch);
		this.creditsButtonText.render(batch);

		this.exitButton.render(batch);
		this.exitButtonText.render(batch);

		this.versionText.render(batch);
	}

	@Override
	public void renderDebug(ShapeRenderer renderer) {

	}

	@Override
	public void pauseScreen() {
		A.getMusic(MusicName.MAIN_MENU).pause();
	}

	@Override
	public void resumeScreen() {
		A.getMusic(MusicName.MAIN_MENU).play();
	}
}
