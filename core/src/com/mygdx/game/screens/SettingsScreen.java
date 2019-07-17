package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.UntitledGame;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.screens.game.Background;
import com.mygdx.game.ui.ButtonUI;
import com.mygdx.game.ui.TextUI;

import static com.mygdx.game.UntitledGame.BUTTON_HEIGHT;
import static com.mygdx.game.UntitledGame.BUTTON_WIDTH;
import static com.mygdx.game.UntitledGame.CAMERA_HEIGHT;
import static com.mygdx.game.UntitledGame.CAMERA_WIDTH;
import static com.mygdx.game.UntitledGame.SETTINGS_FULLSCREEN;
import static com.mygdx.game.UntitledGame.SETTINGS_NAME;
import static com.mygdx.game.UntitledGame.SETTINGS_VSYNC;
import static com.mygdx.game.UntitledGame.SETTINGS_VSYNC_DEFAULT;
import static com.mygdx.game.UntitledGame.WINDOW_HEIGHT;
import static com.mygdx.game.UntitledGame.WINDOW_WIDTH;
import static com.mygdx.game.assets.FontName.MINECRAFT_16;
import static com.mygdx.game.assets.TextureName.BUTTON_HOVER;
import static com.mygdx.game.assets.TextureName.BUTTON_NORMAL;
import static com.mygdx.game.screens.ScreenName.MAIN_MENU;
import static com.mygdx.game.screens.ScreenName.NAME_SETTINGS;
import static com.mygdx.game.ui.UIAlign.LEFT;
import static com.mygdx.game.ui.UIAlign.MIDDLE;

public class SettingsScreen extends UntitledScreen {
	private static final String NAME_TEXT = "YOUR NAME:";
	private static final float NAME_TEXT_X = 50f;
	private static final float NAME_TEXT_Y = CAMERA_HEIGHT - 50f;

	private static final float NAME_BUTTON_X = 320f;
	private static final float NAME_BUTTON_Y = NAME_TEXT_Y;

	private static final String VSYNC_TEXT = "VSYNC:";
	private static final float VSYNC_TEXT_X = 50f;
	private static final float VSYNC_TEXT_Y = NAME_TEXT_Y - 50f;

	private static final float VSYNC_BUTTON_X = NAME_BUTTON_X;
	private static final float VSYNC_BUTTON_Y = VSYNC_TEXT_Y;

	private static final String FULLSCREEN_TEXT = "FULLSCREEN:";
	private static final float FULLSCREEN_TEXT_X = 50f;
	private static final float FULLSCREEN_TEXT_Y = VSYNC_TEXT_Y - 50f;

	private static final float FULLSCREEN_BUTTON_X = NAME_BUTTON_X;
	private static final float FULLSCREEN_BUTTON_Y = FULLSCREEN_TEXT_Y;

	private static final String BUTTON_ON_TEXT = "ON";
	private static final String BUTTON_OFF_TEXT = "OFF";

	private static final String BACK_BUTTON_TEXT = "BACK";
	private static final float BACK_BUTTON_X = CAMERA_WIDTH / 2f;
	private static final float BACK_BUTTON_Y = 50;

	private Preferences settings;
	private Background background;

	private TextUI nameText;
	private ButtonUI nameButton;
	private TextUI nameButtonText;

	private TextUI vsyncText;
	private ButtonUI vsyncButton;
	private TextUI vsyncButtonText;

	private TextUI fullscreenText;
	private ButtonUI fullscreenButton;
	private TextUI fullscreenButtonText;

	private ButtonUI backButton;
	private TextUI backButtonText;

	public SettingsScreen(UntitledGame game) {
		super(game);
		Assets A = game.getAssets();
		Viewport viewport = game.getViewport();
		InputMultiplexer multiplexer = game.getInputMultiplexer();

		this.settings = game.getSettings();
		this.background = new Background(A);

		// Name
		this.nameText = new TextUI(LEFT, A.getFont(MINECRAFT_16))
				.setX(NAME_TEXT_X)
				.setY(NAME_TEXT_Y)
				.setText(NAME_TEXT);

		this.nameButton = new ButtonUI(MIDDLE, viewport, () -> setScreen(NAME_SETTINGS))
				.setX(NAME_BUTTON_X)
				.setY(NAME_BUTTON_Y)
				.setW(BUTTON_WIDTH)
				.setH(BUTTON_HEIGHT)
				.setNormalTexture(A.getTexture(BUTTON_NORMAL))
				.setHoverTexture(A.getTexture(BUTTON_HOVER));

		this.nameButtonText = new TextUI(MIDDLE, A.getFont(MINECRAFT_16))
				.setX(NAME_BUTTON_X)
				.setY(NAME_BUTTON_Y)
				.setColor(Color.GOLD)
				.setText(settings.getString(SETTINGS_NAME));

		// Vsync
		this.vsyncText = new TextUI(LEFT, A.getFont(MINECRAFT_16))
				.setX(VSYNC_TEXT_X)
				.setY(VSYNC_TEXT_Y)
				.setText(VSYNC_TEXT);

		this.vsyncButton = new ButtonUI(MIDDLE, viewport, () -> {
			boolean vsync = settings.getBoolean(SETTINGS_VSYNC, SETTINGS_VSYNC_DEFAULT);
			Gdx.graphics.setVSync(!vsync);
			settings.putBoolean(SETTINGS_VSYNC, !vsync);
			settings.flush();
			updateVsyncText();
		})
				.setX(VSYNC_BUTTON_X)
				.setY(VSYNC_BUTTON_Y)
				.setW(BUTTON_WIDTH)
				.setH(BUTTON_HEIGHT)
				.setNormalTexture(A.getTexture(BUTTON_NORMAL))
				.setHoverTexture(A.getTexture(BUTTON_HOVER));

		this.vsyncButtonText = new TextUI(MIDDLE, A.getFont(MINECRAFT_16))
				.setX(VSYNC_BUTTON_X)
				.setY(VSYNC_BUTTON_Y)
				.setColor(Color.GOLD);

		updateVsyncText();

		// Fullscreen
		this.fullscreenText = new TextUI(LEFT, A.getFont(MINECRAFT_16))
				.setX(FULLSCREEN_TEXT_X)
				.setY(FULLSCREEN_TEXT_Y)
				.setText(FULLSCREEN_TEXT);

		this.fullscreenButton = new ButtonUI(MIDDLE, viewport, () -> {
			if (Gdx.graphics.isFullscreen()) {
				Gdx.graphics.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
			} else {
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			}

			game.getSettings().putBoolean(SETTINGS_FULLSCREEN, Gdx.graphics.isFullscreen());
			game.getSettings().flush();
			updateFullscreenText();
		})
				.setX(FULLSCREEN_BUTTON_X)
				.setY(FULLSCREEN_BUTTON_Y)
				.setW(BUTTON_WIDTH)
				.setH(BUTTON_HEIGHT)
				.setNormalTexture(A.getTexture(BUTTON_NORMAL))
				.setHoverTexture(A.getTexture(BUTTON_HOVER));

		this.fullscreenButtonText = new TextUI(MIDDLE, A.getFont(MINECRAFT_16))
				.setX(FULLSCREEN_BUTTON_X)
				.setY(FULLSCREEN_BUTTON_Y)
				.setColor(Color.GOLD);

		updateFullscreenText();

		// Back
		this.backButton = new ButtonUI(MIDDLE, viewport, () -> setScreen(MAIN_MENU))
				.setX(BACK_BUTTON_X)
				.setY(BACK_BUTTON_Y)
				.setW(BUTTON_WIDTH)
				.setH(BUTTON_HEIGHT)
				.setNormalTexture(A.getTexture(BUTTON_NORMAL))
				.setHoverTexture(A.getTexture(BUTTON_HOVER));

		this.backButtonText = new TextUI(MIDDLE, A.getFont(MINECRAFT_16))
				.setX(BACK_BUTTON_X)
				.setY(BACK_BUTTON_Y)
				.setText(BACK_BUTTON_TEXT);

		// Add input processors
		multiplexer.addProcessor(nameButton);
		multiplexer.addProcessor(vsyncButton);
		multiplexer.addProcessor(fullscreenButton);
		multiplexer.addProcessor(backButton);
	}

	private void updateFullscreenText() {
		if (Gdx.graphics.isFullscreen()) {
			this.fullscreenButtonText.setText(BUTTON_ON_TEXT);
		} else {
			this.fullscreenButtonText.setText(BUTTON_OFF_TEXT);
		}
	}

	private void updateVsyncText() {
		if (settings.getBoolean(SETTINGS_VSYNC, SETTINGS_VSYNC_DEFAULT)) {
			this.vsyncButtonText.setText(BUTTON_ON_TEXT);
		} else {
			this.vsyncButtonText.setText(BUTTON_OFF_TEXT);
		}
	}

	@Override
	public void update() {

	}

	@Override
	public void render(SpriteBatch batch) {
		this.background.render(batch);

		this.nameText.render(batch);
		this.nameButton.render(batch);
		this.nameButtonText.render(batch);

		this.vsyncText.render(batch);
		this.vsyncButton.render(batch);
		this.vsyncButtonText.render(batch);

		this.fullscreenText.render(batch);
		this.fullscreenButton.render(batch);
		this.fullscreenButtonText.render(batch);

		this.backButton.render(batch);
		this.backButtonText.render(batch);
	}

	@Override
	public void renderDebug(ShapeRenderer renderer) {

	}
}
