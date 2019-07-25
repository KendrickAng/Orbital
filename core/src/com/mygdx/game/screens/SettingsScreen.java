package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.UntitledGame;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.assets.MusicName;
import com.mygdx.game.ui.ButtonUI;
import com.mygdx.game.ui.TextUI;

import static com.mygdx.game.UntitledGame.BUTTON_H;
import static com.mygdx.game.UntitledGame.BUTTON_W;
import static com.mygdx.game.UntitledGame.CAMERA_HEIGHT;
import static com.mygdx.game.UntitledGame.CAMERA_WIDTH;
import static com.mygdx.game.UntitledGame.SETTINGS_FULLSCREEN;
import static com.mygdx.game.UntitledGame.SETTINGS_FULLSCREEN_DEFAULT;
import static com.mygdx.game.UntitledGame.SETTINGS_MUSIC_VOLUME;
import static com.mygdx.game.UntitledGame.SETTINGS_MUSIC_VOLUME_DEFAULT;
import static com.mygdx.game.UntitledGame.SETTINGS_NAME;
import static com.mygdx.game.UntitledGame.SETTINGS_VSYNC;
import static com.mygdx.game.UntitledGame.SETTINGS_VSYNC_DEFAULT;
import static com.mygdx.game.UntitledGame.WINDOW_HEIGHT;
import static com.mygdx.game.UntitledGame.WINDOW_WIDTH;
import static com.mygdx.game.assets.FontName.MINECRAFT_8;
import static com.mygdx.game.assets.TextureName.BUTTON_HOVER;
import static com.mygdx.game.assets.TextureName.BUTTON_NORMAL;
import static com.mygdx.game.assets.TextureName.MENU_BACKGROUND;
import static com.mygdx.game.screens.ScreenName.MAIN_MENU;
import static com.mygdx.game.screens.ScreenName.NAME_SETTINGS;
import static com.mygdx.game.ui.UIAlign.LEFT;
import static com.mygdx.game.ui.UIAlign.MIDDLE;

public class SettingsScreen extends UntitledScreen {
	private static final String NAME_TEXT = "YOUR NAME:";
	private static final float NAME_TEXT_X = 50f;
	private static final float NAME_TEXT_Y = CAMERA_HEIGHT - 50f;

	private static final float NAME_BUTTON_X = 200f;
	private static final float NAME_BUTTON_Y = NAME_TEXT_Y;

	private static final String VSYNC_TEXT = "VSYNC:";
	private static final float VSYNC_TEXT_X = 50f;
	private static final float VSYNC_TEXT_Y = NAME_TEXT_Y - 50f;

	private static final float VSYNC_BUTTON_X = NAME_BUTTON_X;
	private static final float VSYNC_BUTTON_Y = VSYNC_TEXT_Y;

	private static final String FULLSCREEN_TEXT = "FULLSCREEN:";
	private static final float FULLSCREEN_TEXT_X = 50f;
	private static final float FULLSCREEN_TEXT_Y = VSYNC_TEXT_Y - 30f;

	private static final float FULLSCREEN_BUTTON_X = NAME_BUTTON_X;
	private static final float FULLSCREEN_BUTTON_Y = FULLSCREEN_TEXT_Y;

	private static final String MUSIC_VOLUME_TEXT = "MUSIC VOLUME:";
	private static final float MUSIC_VOLUME_TEXT_X = 50f;
	private static final float MUSIC_VOLUME_TEXT_Y = FULLSCREEN_BUTTON_Y - 50f;

	private static final String MUSIC_VOLUME_MINUS_TEXT = "-";
	private static final float MUSIC_VOLUME_MINUS_BUTTON_X = NAME_BUTTON_X - 30f;
	private static final float MUSIC_VOLUME_MINUS_BUTTON_Y = MUSIC_VOLUME_TEXT_Y;

	private static final float MUSIC_VOLUME_PERCENTAGE_TEXT_X = NAME_BUTTON_X;
	private static final float MUSIC_VOLUME_PERCENTAGE_TEXT_Y = MUSIC_VOLUME_TEXT_Y;

	private static final String MUSIC_VOLUME_PLUS_TEXT = "+";
	private static final float MUSIC_VOLUME_PLUS_BUTTON_X = NAME_BUTTON_X + 30f;
	private static final float MUSIC_VOLUME_PLUS_BUTTON_Y = MUSIC_VOLUME_TEXT_Y;

	private static final int VOLUME_DELTA = 5;

	private static final String BUTTON_ON_TEXT = "ON";
	private static final String BUTTON_OFF_TEXT = "OFF";

	private static final String SAVE_BUTTON_TEXT = "SAVE";
	private static final float SAVE_BUTTON_X = CAMERA_WIDTH / 2f;
	private static final float SAVE_BUTTON_Y = 50;

	private Assets A;
	private OrthographicCamera camera;
	private Preferences settings;

	private TextUI nameText;
	private ButtonUI nameButton;
	private TextUI nameButtonText;

	private TextUI vsyncText;
	private ButtonUI vsyncButton;
	private TextUI vsyncButtonText;
	private boolean vsyncSetting;

	private TextUI fullscreenText;
	private ButtonUI fullscreenButton;
	private TextUI fullscreenButtonText;
	private boolean fullscreenSetting;

	private TextUI musicVolumeText;
	private ButtonUI musicVolumeMinusButton;
	private TextUI musicVolumeMinusButtonText;
	private TextUI musicVolumePercentageText;
	private ButtonUI musicVolumePlusButton;
	private TextUI musicVolumePlusButtonText;
	private int musicVolumeSetting;

	private ButtonUI saveButton;
	private TextUI saveButtonText;

	public SettingsScreen(UntitledGame game) {
		super(game);
		Viewport viewport = game.getViewport();
		InputMultiplexer multiplexer = game.getInputMultiplexer();

		this.A = game.getAssets();
		this.camera = game.getCamera();
		this.settings = game.getSettings();

		this.vsyncSetting = settings.getBoolean(SETTINGS_VSYNC, SETTINGS_VSYNC_DEFAULT);
		this.fullscreenSetting = settings.getBoolean(SETTINGS_FULLSCREEN, SETTINGS_FULLSCREEN_DEFAULT);
		this.musicVolumeSetting = settings.getInteger(SETTINGS_MUSIC_VOLUME, SETTINGS_MUSIC_VOLUME_DEFAULT);

		// Name
		this.nameText = new TextUI(LEFT, A.getFont(MINECRAFT_8))
				.setX(NAME_TEXT_X)
				.setY(NAME_TEXT_Y)
				.setText(NAME_TEXT);

		this.nameButton = new ButtonUI(MIDDLE, viewport, () -> setScreen(NAME_SETTINGS))
				.setX(NAME_BUTTON_X)
				.setY(NAME_BUTTON_Y)
				.setW(BUTTON_W)
				.setH(BUTTON_H)
				.setNormalTexture(A.getTexture(BUTTON_NORMAL))
				.setHoverTexture(A.getTexture(BUTTON_HOVER));

		this.nameButtonText = new TextUI(MIDDLE, A.getFont(MINECRAFT_8))
				.setX(NAME_BUTTON_X)
				.setY(NAME_BUTTON_Y)
				.setColor(Color.GOLD)
				.setText(settings.getString(SETTINGS_NAME));

		// Vsync
		this.vsyncText = new TextUI(LEFT, A.getFont(MINECRAFT_8))
				.setX(VSYNC_TEXT_X)
				.setY(VSYNC_TEXT_Y)
				.setText(VSYNC_TEXT);

		this.vsyncButton = new ButtonUI(MIDDLE, viewport, () -> {
			vsyncSetting = !vsyncSetting;
			Gdx.graphics.setVSync(vsyncSetting);
			updateVsyncText();
		})
				.setX(VSYNC_BUTTON_X)
				.setY(VSYNC_BUTTON_Y)
				.setW(BUTTON_W)
				.setH(BUTTON_H)
				.setNormalTexture(A.getTexture(BUTTON_NORMAL))
				.setHoverTexture(A.getTexture(BUTTON_HOVER));

		this.vsyncButtonText = new TextUI(MIDDLE, A.getFont(MINECRAFT_8))
				.setX(VSYNC_BUTTON_X)
				.setY(VSYNC_BUTTON_Y)
				.setColor(Color.GOLD);

		updateVsyncText();

		// Fullscreen
		this.fullscreenText = new TextUI(LEFT, A.getFont(MINECRAFT_8))
				.setX(FULLSCREEN_TEXT_X)
				.setY(FULLSCREEN_TEXT_Y)
				.setText(FULLSCREEN_TEXT);

		this.fullscreenButton = new ButtonUI(MIDDLE, viewport, () -> {
			if (Gdx.graphics.isFullscreen()) {
				Gdx.graphics.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
			} else {
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			}
			fullscreenSetting = Gdx.graphics.isFullscreen();
			updateFullscreenText();
		})
				.setX(FULLSCREEN_BUTTON_X)
				.setY(FULLSCREEN_BUTTON_Y)
				.setW(BUTTON_W)
				.setH(BUTTON_H)
				.setNormalTexture(A.getTexture(BUTTON_NORMAL))
				.setHoverTexture(A.getTexture(BUTTON_HOVER));

		this.fullscreenButtonText = new TextUI(MIDDLE, A.getFont(MINECRAFT_8))
				.setX(FULLSCREEN_BUTTON_X)
				.setY(FULLSCREEN_BUTTON_Y)
				.setColor(Color.GOLD);

		updateFullscreenText();

		// Music Volume
		this.musicVolumeText = new TextUI(LEFT, A.getFont(MINECRAFT_8))
				.setX(MUSIC_VOLUME_TEXT_X)
				.setY(MUSIC_VOLUME_TEXT_Y)
				.setText(MUSIC_VOLUME_TEXT);

		this.musicVolumeMinusButton = new ButtonUI(MIDDLE, viewport, () -> {
			if (musicVolumeSetting > 0) {
				musicVolumeSetting -= VOLUME_DELTA;
			}
			updateMusicVolumeText();
		})
				.setX(MUSIC_VOLUME_MINUS_BUTTON_X)
				.setY(MUSIC_VOLUME_MINUS_BUTTON_Y)
				.setW(BUTTON_H)
				.setH(BUTTON_H)
				.setNormalTexture(A.getTexture(BUTTON_NORMAL))
				.setHoverTexture(A.getTexture(BUTTON_HOVER));

		this.musicVolumeMinusButtonText = new TextUI(MIDDLE, A.getFont(MINECRAFT_8))
				.setX(MUSIC_VOLUME_MINUS_BUTTON_X)
				.setY(MUSIC_VOLUME_MINUS_BUTTON_Y)
				.setText(MUSIC_VOLUME_MINUS_TEXT);

		this.musicVolumePercentageText = new TextUI(MIDDLE, A.getFont(MINECRAFT_8))
				.setX(MUSIC_VOLUME_PERCENTAGE_TEXT_X)
				.setY(MUSIC_VOLUME_PERCENTAGE_TEXT_Y)
				.setColor(Color.GOLD);

		this.musicVolumePlusButton = new ButtonUI(MIDDLE, viewport, () -> {
			if (musicVolumeSetting < 100) {
				musicVolumeSetting += VOLUME_DELTA;
			}
			updateMusicVolumeText();
		})
				.setX(MUSIC_VOLUME_PLUS_BUTTON_X)
				.setY(MUSIC_VOLUME_PLUS_BUTTON_Y)
				.setW(BUTTON_H)
				.setH(BUTTON_H)
				.setNormalTexture(A.getTexture(BUTTON_NORMAL))
				.setHoverTexture(A.getTexture(BUTTON_HOVER));

		this.musicVolumePlusButtonText = new TextUI(MIDDLE, A.getFont(MINECRAFT_8))
				.setX(MUSIC_VOLUME_PLUS_BUTTON_X)
				.setY(MUSIC_VOLUME_PLUS_BUTTON_Y)
				.setText(MUSIC_VOLUME_PLUS_TEXT);

		updateMusicVolumeText();

		// Save
		this.saveButton = new ButtonUI(MIDDLE, viewport, () -> {
			settings.putBoolean(SETTINGS_VSYNC, vsyncSetting);
			settings.putBoolean(SETTINGS_FULLSCREEN, fullscreenSetting);
			settings.putInteger(SETTINGS_MUSIC_VOLUME, musicVolumeSetting);
			settings.flush();
			setScreen(MAIN_MENU);
		})
				.setX(SAVE_BUTTON_X)
				.setY(SAVE_BUTTON_Y)
				.setW(BUTTON_W)
				.setH(BUTTON_H)
				.setNormalTexture(A.getTexture(BUTTON_NORMAL))
				.setHoverTexture(A.getTexture(BUTTON_HOVER));

		this.saveButtonText = new TextUI(MIDDLE, A.getFont(MINECRAFT_8))
				.setX(SAVE_BUTTON_X)
				.setY(SAVE_BUTTON_Y)
				.setText(SAVE_BUTTON_TEXT);

		// Add input processors
		multiplexer.addProcessor(nameButton);
		multiplexer.addProcessor(vsyncButton);
		multiplexer.addProcessor(fullscreenButton);
		multiplexer.addProcessor(musicVolumeMinusButton);
		multiplexer.addProcessor(musicVolumePlusButton);
		multiplexer.addProcessor(saveButton);
	}

	private void updateVsyncText() {
		if (vsyncSetting) {
			this.vsyncButtonText.setText(BUTTON_ON_TEXT);
		} else {
			this.vsyncButtonText.setText(BUTTON_OFF_TEXT);
		}
	}

	private void updateFullscreenText() {
		if (fullscreenSetting) {
			this.fullscreenButtonText.setText(BUTTON_ON_TEXT);
		} else {
			this.fullscreenButtonText.setText(BUTTON_OFF_TEXT);
		}
	}

	private void updateMusicVolumeText() {
		this.musicVolumePercentageText.setText(musicVolumeSetting + "%");
		A.getMusic(MusicName.MAIN_MENU).setVolume(musicVolumeSetting / 100f);
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		batch.draw(A.getTexture(MENU_BACKGROUND), 0, 0, 0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		this.nameText.render(batch);
		this.nameButton.render(batch);
		this.nameButtonText.render(batch);

		this.vsyncText.render(batch);
		this.vsyncButton.render(batch);
		this.vsyncButtonText.render(batch);

		this.fullscreenText.render(batch);
		this.fullscreenButton.render(batch);
		this.fullscreenButtonText.render(batch);

		this.musicVolumeText.render(batch);
		this.musicVolumeMinusButton.render(batch);
		this.musicVolumeMinusButtonText.render(batch);
		this.musicVolumePercentageText.render(batch);
		this.musicVolumePlusButton.render(batch);
		this.musicVolumePlusButtonText.render(batch);

		this.saveButton.render(batch);
		this.saveButtonText.render(batch);

		batch.end();
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
