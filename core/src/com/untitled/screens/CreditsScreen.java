package com.untitled.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.untitled.UntitledGame;
import com.untitled.assets.Assets;
import com.untitled.assets.FontName;
import com.untitled.assets.MusicName;
import com.untitled.assets.TextureName;
import com.untitled.ui.ButtonUI;
import com.untitled.ui.TextUI;
import com.untitled.ui.TextUIAlign;
import com.untitled.ui.UIAlign;

public class CreditsScreen extends UntitledScreen {
	private static final String MUSIC_TEXT = "MENU MUSIC:\nTHE FALL OF ARCANA" +
			"\n\nBOSS MUSIC:\nHEROIC DEMISE" +
			"\n\nMUSIC BY MATTHEW PABLO\nWWW.MATTHEWPABLO.COM";

	private static final float MUSIC_TEXT_X = UntitledGame.CAMERA_WIDTH / 2f;
	private static final float MUSIC_TEXT_Y = UntitledGame.CAMERA_HEIGHT - 50f;

	private static final String BACK_BUTTON_TEXT = "BACK";
	private static final float BACK_BUTTON_X = UntitledGame.CAMERA_WIDTH / 2f;
	private static final float BACK_BUTTON_Y = 50;

	private Assets A;
	private OrthographicCamera camera;

	private TextUI musicText;

	private ButtonUI backButton;
	private TextUI backButtonText;

	public CreditsScreen(UntitledGame game) {
		super(game);

		this.A = game.getAssets();
		this.camera = game.getCamera();

		Viewport viewport = game.getViewport();
		InputMultiplexer multiplexer = game.getInputMultiplexer();

		this.musicText = new TextUI(UIAlign.TOP_MIDDLE, A.getFont(FontName.MINECRAFT_8))
				.setX(MUSIC_TEXT_X)
				.setY(MUSIC_TEXT_Y)
				.setText(MUSIC_TEXT)
				.setTextAlign(TextUIAlign.MIDDLE);

		// Back
		this.backButton = new ButtonUI(UIAlign.MIDDLE, viewport, () -> setScreen(ScreenName.MAIN_MENU))
				.setX(BACK_BUTTON_X)
				.setY(BACK_BUTTON_Y)
				.setW(UntitledGame.BUTTON_W)
				.setH(UntitledGame.BUTTON_H)
				.setNormalTexture(A.getTexture(TextureName.BUTTON_NORMAL))
				.setHoverTexture(A.getTexture(TextureName.BUTTON_HOVER));

		this.backButtonText = new TextUI(UIAlign.MIDDLE, A.getFont(FontName.MINECRAFT_8))
				.setX(BACK_BUTTON_X)
				.setY(BACK_BUTTON_Y)
				.setText(BACK_BUTTON_TEXT);

		// Add input processors
		multiplexer.addProcessor(backButton);
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		batch.draw(A.getTexture(TextureName.MENU_BACKGROUND), 0, 0, 0, 0, UntitledGame.CAMERA_WIDTH, UntitledGame.CAMERA_HEIGHT);

		this.musicText.render(batch);

		this.backButton.render(batch);
		this.backButtonText.render(batch);

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
