package com.mygdx.game.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.UntitledGame;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.assets.MusicName;
import com.mygdx.game.ui.ButtonUI;
import com.mygdx.game.ui.TextUI;
import com.mygdx.game.ui.TextUIAlign;

import static com.mygdx.game.UntitledGame.BUTTON_H;
import static com.mygdx.game.UntitledGame.BUTTON_W;
import static com.mygdx.game.UntitledGame.CAMERA_HEIGHT;
import static com.mygdx.game.UntitledGame.CAMERA_WIDTH;
import static com.mygdx.game.assets.FontName.MINECRAFT_8;
import static com.mygdx.game.assets.TextureName.BUTTON_HOVER;
import static com.mygdx.game.assets.TextureName.BUTTON_NORMAL;
import static com.mygdx.game.assets.TextureName.MENU_BACKGROUND;
import static com.mygdx.game.screens.ScreenName.MAIN_MENU;
import static com.mygdx.game.ui.UIAlign.MIDDLE;
import static com.mygdx.game.ui.UIAlign.TOP_MIDDLE;

public class CreditsScreen extends UntitledScreen {
	private static final String MUSIC_TEXT = "MENU MUSIC:\nTHE FALL OF ARCANA" +
			"\n\nBOSS MUSIC:\nHEROIC DEMISE" +
			"\n\nMUSIC BY MATTHEW PABLO\nWWW.MATTHEWPABLO.COM";

	private static final float MUSIC_TEXT_X = CAMERA_WIDTH / 2f;
	private static final float MUSIC_TEXT_Y = CAMERA_HEIGHT - 50f;

	private static final String BACK_BUTTON_TEXT = "BACK";
	private static final float BACK_BUTTON_X = CAMERA_WIDTH / 2f;
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

		this.musicText = new TextUI(TOP_MIDDLE, A.getFont(MINECRAFT_8))
				.setX(MUSIC_TEXT_X)
				.setY(MUSIC_TEXT_Y)
				.setText(MUSIC_TEXT)
				.setTextAlign(TextUIAlign.MIDDLE);

		// Back
		this.backButton = new ButtonUI(MIDDLE, viewport, () -> setScreen(MAIN_MENU))
				.setX(BACK_BUTTON_X)
				.setY(BACK_BUTTON_Y)
				.setW(BUTTON_W)
				.setH(BUTTON_H)
				.setNormalTexture(A.getTexture(BUTTON_NORMAL))
				.setHoverTexture(A.getTexture(BUTTON_HOVER));

		this.backButtonText = new TextUI(MIDDLE, A.getFont(MINECRAFT_8))
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

		batch.draw(A.getTexture(MENU_BACKGROUND), 0, 0, 0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

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
