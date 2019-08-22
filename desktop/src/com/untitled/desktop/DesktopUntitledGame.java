package com.untitled.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.untitled.UntitledGame;
import com.untitled.assets.Assets;
import com.untitled.assets.TextureName;
import com.untitled.desktop.screens.DesktopGameScreen;
import com.untitled.desktop.screens.DesktopSettingsScreen;
import com.untitled.screens.CreditsScreen;
import com.untitled.screens.HighscoresScreen;
import com.untitled.screens.MainMenuScreen;
import com.untitled.screens.NameScreen;
import com.untitled.screens.ScreenName;

import static com.untitled.screens.ScreenName.MAIN_MENU;
import static com.untitled.screens.ScreenName.NAME_MENU;
import static com.untitled.screens.ScreenName.SETTINGS;

public class DesktopUntitledGame extends UntitledGame {

	@Override
	protected void loadAssets(Assets A) {
		A.loadTexture(TextureName.DESKTOP_COOLDOWN_0);
		A.loadTexture(TextureName.DESKTOP_COOLDOWN_1);
		A.loadTexture(TextureName.DESKTOP_COOLDOWN_2);
		A.loadTexture(TextureName.DESKTOP_COOLDOWN_3);
		A.loadTexture(TextureName.DESKTOP_COOLDOWN_4);
		A.loadTexture(TextureName.DESKTOP_COOLDOWN_5);

		A.loadTexture(TextureName.DESKTOP_COOLDOWN_BLOCK);
		A.loadTexture(TextureName.DESKTOP_COOLDOWN_HAMMER_SWING);
		A.loadTexture(TextureName.DESKTOP_COOLDOWN_FORTRESS);
		A.loadTexture(TextureName.DESKTOP_COOLDOWN_DASH);
		A.loadTexture(TextureName.DESKTOP_COOLDOWN_SHURIKEN_THROW);
		A.loadTexture(TextureName.DESKTOP_COOLDOWN_CLEANSE);
		A.loadTexture(TextureName.DESKTOP_COOLDOWN_SWITCH_CHARACTER);
	}

	@Override
	protected void createAbstract() {
		/* Settings */
		Preferences settings = getSettings();

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
			setName(name);
			setScreen(MAIN_MENU);
		}
	}

	@Override
	protected void setScreenAbstract(ScreenName screen) {
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
				setScreen(new DesktopGameScreen(this));
				break;
			case HIGHSCORES:
				setScreen(new HighscoresScreen(this));
				break;
			case SETTINGS:
				setScreen(new DesktopSettingsScreen(this));
				break;
			case CREDITS:
				setScreen(new CreditsScreen(this));
				break;
		}
	}
}
