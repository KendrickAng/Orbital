package com.untitled.android.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.untitled.UntitledGame;
import com.untitled.assets.Assets;
import com.untitled.assets.FontName;
import com.untitled.assets.MusicName;
import com.untitled.assets.TextureName;
import com.untitled.screens.SettingsScreen;
import com.untitled.ui.TextUI;
import com.untitled.ui.UIAlign;
import com.untitled.ui.button.ButtonUI;

import static com.untitled.UntitledGame.BUTTON_H;
import static com.untitled.UntitledGame.BUTTON_W;
import static com.untitled.UntitledGame.CAMERA_HEIGHT;
import static com.untitled.UntitledGame.CAMERA_WIDTH;
import static com.untitled.UntitledGame.SETTINGS_MUSIC_VOLUME;
import static com.untitled.UntitledGame.SETTINGS_MUSIC_VOLUME_DEFAULT;
import static com.untitled.screens.ScreenName.MAIN_MENU;

/**
 * Setttings screen of Untitled.
 */
public class AndroidSettingsScreen extends SettingsScreen {
	private static final String MUSIC_VOLUME_TEXT = "MUSIC VOLUME:";
	private static final float MUSIC_VOLUME_TEXT_X = 50f;
	private static final float MUSIC_VOLUME_TEXT_Y = CAMERA_HEIGHT - 50f;

	private static final String MUSIC_VOLUME_MINUS_TEXT = "-";
	private static final float MUSIC_VOLUME_MINUS_BUTTON_X = 200f - 30f;
	private static final float MUSIC_VOLUME_MINUS_BUTTON_Y = MUSIC_VOLUME_TEXT_Y;

	private static final float MUSIC_VOLUME_PERCENTAGE_TEXT_X = 200f;
	private static final float MUSIC_VOLUME_PERCENTAGE_TEXT_Y = MUSIC_VOLUME_TEXT_Y;

	private static final String MUSIC_VOLUME_PLUS_TEXT = "+";
	private static final float MUSIC_VOLUME_PLUS_BUTTON_X = 200f + 30f;
	private static final float MUSIC_VOLUME_PLUS_BUTTON_Y = MUSIC_VOLUME_TEXT_Y;

	private static final int VOLUME_DELTA = 5;

	private static final String SAVE_BUTTON_TEXT = "SAVE";
	private static final float SAVE_BUTTON_X = CAMERA_WIDTH / 2f;
	private static final float SAVE_BUTTON_Y = 50;

	private Assets A;
	private OrthographicCamera camera;
	private Preferences settings;

	private TextUI musicVolumeText;
	private ButtonUI musicVolumeMinusButton;
	private TextUI musicVolumeMinusButtonText;
	private TextUI musicVolumePercentageText;
	private ButtonUI musicVolumePlusButton;
	private TextUI musicVolumePlusButtonText;
	private int musicVolumeSetting;

	private ButtonUI saveButton;
	private TextUI saveButtonText;

	public AndroidSettingsScreen(UntitledGame game) {
		super(game);
		Viewport viewport = game.getViewport();
		InputMultiplexer multiplexer = game.getInputMultiplexer();

		this.A = game.getAssets();
		this.camera = game.getCamera();
		this.settings = game.getSettings();

		this.musicVolumeSetting = settings.getInteger(SETTINGS_MUSIC_VOLUME, SETTINGS_MUSIC_VOLUME_DEFAULT);

		// Music Volume
		this.musicVolumeText = new TextUI(UIAlign.LEFT, A.getFont(FontName.MINECRAFT_8))
				.setX(MUSIC_VOLUME_TEXT_X)
				.setY(MUSIC_VOLUME_TEXT_Y)
				.setText(MUSIC_VOLUME_TEXT);

		this.musicVolumeMinusButton = new ButtonUI(UIAlign.MIDDLE, viewport)
				.setButtonUp(() -> {
					if (musicVolumeSetting > 0) {
						musicVolumeSetting -= VOLUME_DELTA;
					}
					updateMusicVolumeText();
				})
				.setX(MUSIC_VOLUME_MINUS_BUTTON_X)
				.setY(MUSIC_VOLUME_MINUS_BUTTON_Y)
				.setW(BUTTON_H)
				.setH(BUTTON_H)
				.setNormalTexture(A.getTexture(TextureName.BUTTON_NORMAL))
				.setHoverTexture(A.getTexture(TextureName.BUTTON_HOVER));

		this.musicVolumeMinusButtonText = new TextUI(UIAlign.MIDDLE, A.getFont(FontName.MINECRAFT_8))
				.setX(MUSIC_VOLUME_MINUS_BUTTON_X)
				.setY(MUSIC_VOLUME_MINUS_BUTTON_Y)
				.setText(MUSIC_VOLUME_MINUS_TEXT);

		this.musicVolumePercentageText = new TextUI(UIAlign.MIDDLE, A.getFont(FontName.MINECRAFT_8))
				.setX(MUSIC_VOLUME_PERCENTAGE_TEXT_X)
				.setY(MUSIC_VOLUME_PERCENTAGE_TEXT_Y)
				.setColor(Color.GOLD);

		this.musicVolumePlusButton = new ButtonUI(UIAlign.MIDDLE, viewport)
				.setButtonUp(() -> {
					if (musicVolumeSetting < 100) {
						musicVolumeSetting += VOLUME_DELTA;
					}
					updateMusicVolumeText();
				})
				.setX(MUSIC_VOLUME_PLUS_BUTTON_X)
				.setY(MUSIC_VOLUME_PLUS_BUTTON_Y)
				.setW(BUTTON_H)
				.setH(BUTTON_H)
				.setNormalTexture(A.getTexture(TextureName.BUTTON_NORMAL))
				.setHoverTexture(A.getTexture(TextureName.BUTTON_HOVER));

		this.musicVolumePlusButtonText = new TextUI(UIAlign.MIDDLE, A.getFont(FontName.MINECRAFT_8))
				.setX(MUSIC_VOLUME_PLUS_BUTTON_X)
				.setY(MUSIC_VOLUME_PLUS_BUTTON_Y)
				.setText(MUSIC_VOLUME_PLUS_TEXT);

		updateMusicVolumeText();

		// Save
		this.saveButton = new ButtonUI(UIAlign.MIDDLE, viewport)
				.setButtonUp(() -> {
					settings.putInteger(SETTINGS_MUSIC_VOLUME, musicVolumeSetting);
					settings.flush();
					setScreen(MAIN_MENU);
				})
				.setX(SAVE_BUTTON_X)
				.setY(SAVE_BUTTON_Y)
				.setW(BUTTON_W)
				.setH(BUTTON_H)
				.setNormalTexture(A.getTexture(TextureName.BUTTON_NORMAL))
				.setHoverTexture(A.getTexture(TextureName.BUTTON_HOVER));

		this.saveButtonText = new TextUI(UIAlign.MIDDLE, A.getFont(FontName.MINECRAFT_8))
				.setX(SAVE_BUTTON_X)
				.setY(SAVE_BUTTON_Y)
				.setText(SAVE_BUTTON_TEXT);

		// Add input processors
		multiplexer.addProcessor(musicVolumeMinusButton);
		multiplexer.addProcessor(musicVolumePlusButton);
		multiplexer.addProcessor(saveButton);
	}

	private void updateMusicVolumeText() {
		this.musicVolumePercentageText.setText(musicVolumeSetting + "%");
		A.getMusic(MusicName.MAIN_MENU).setVolume(musicVolumeSetting / 100f);
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		batch.draw(A.getTexture(TextureName.MENU_BACKGROUND), 0, 0, 0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

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
}
