package com.untitled.android;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.untitled.UntitledGame;
import com.untitled.android.screens.AndroidGameScreen;
import com.untitled.android.screens.AndroidSettingsScreen;
import com.untitled.assets.Assets;
import com.untitled.assets.TextureName;
import com.untitled.screens.CreditsScreen;
import com.untitled.screens.HighscoresScreen;
import com.untitled.screens.MainMenuScreen;
import com.untitled.screens.ScreenName;

import static com.untitled.screens.ScreenName.MAIN_MENU;

public class AndroidUntitledGame extends UntitledGame {

	@Override
	protected void loadAssets(Assets A) {
		A.loadTexture(TextureName.ANDROID_CONTROLLER);

		A.loadTexture(TextureName.ANDROID_COOLDOWN_0);
		A.loadTexture(TextureName.ANDROID_COOLDOWN_1);
		A.loadTexture(TextureName.ANDROID_COOLDOWN_2);
		A.loadTexture(TextureName.ANDROID_COOLDOWN_3);
		A.loadTexture(TextureName.ANDROID_COOLDOWN_4);
		A.loadTexture(TextureName.ANDROID_COOLDOWN_5);

		A.loadTexture(TextureName.ANDROID_COOLDOWN_BLOCK);
		A.loadTexture(TextureName.ANDROID_COOLDOWN_HAMMER_SWING);
		A.loadTexture(TextureName.ANDROID_COOLDOWN_FORTRESS);
		A.loadTexture(TextureName.ANDROID_COOLDOWN_DASH);
		A.loadTexture(TextureName.ANDROID_COOLDOWN_SHURIKEN_THROW);
		A.loadTexture(TextureName.ANDROID_COOLDOWN_CLEANSE);
		A.loadTexture(TextureName.ANDROID_COOLDOWN_SWITCH_CHARACTER);
	}

	@Override
	protected void createAbstract() {
		/* Settings */
		Preferences settings = getSettings();

		// Name
		String name = settings.getString(SETTINGS_NAME, null);

		if (name == null) {
			// Create a random 3 character name.
			StringBuilder nameBuilder = new StringBuilder();
			for (int i = 0; i < 3; i++) {
				nameBuilder.append((char) MathUtils.random(65, 90));
			}

			name = nameBuilder.toString();
			settings.putString(SETTINGS_NAME, name);
			settings.flush();
		}

		setName(name);
		setScreen(MAIN_MENU);
	}

	@Override
	protected void setScreenAbstract(ScreenName screen) {
		switch (screen) {
			case MAIN_MENU:
				setScreen(new MainMenuScreen(this));
				break;
			case GAME:
				setScreen(new AndroidGameScreen(this));
				break;
			case HIGHSCORES:
				setScreen(new HighscoresScreen(this));
				break;
			case SETTINGS:
				setScreen(new AndroidSettingsScreen(this));
				break;
			case CREDITS:
				setScreen(new CreditsScreen(this));
				break;
		}
	}
}
