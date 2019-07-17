package com.mygdx.game.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.UntitledGame;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.ui.ButtonUI;
import com.mygdx.game.ui.TextUI;

import static com.mygdx.game.UntitledGame.BUTTON_H;
import static com.mygdx.game.UntitledGame.BUTTON_W;
import static com.mygdx.game.UntitledGame.CAMERA_HEIGHT;
import static com.mygdx.game.UntitledGame.CAMERA_WIDTH;
import static com.mygdx.game.assets.FontName.MINECRAFT_8;
import static com.mygdx.game.assets.TextureName.BUTTON_HOVER;
import static com.mygdx.game.assets.TextureName.BUTTON_NORMAL;
import static com.mygdx.game.screens.ScreenName.MAIN_MENU;
import static com.mygdx.game.ui.UIAlign.MIDDLE;
import static com.mygdx.game.ui.UIAlign.TOP_LEFT;

public class CreditsScreen extends UntitledScreen {
	private static final String MENU_MUSIC_TEXT = "MAIN MENU MUSIC:\n\nSAD DAY - BY HEATLYBROS";
	private static final float MENU_MUSIC_TEXT_X = 200f;
	private static final float MENU_MUSIC_TEXT_Y = CAMERA_HEIGHT - 50f;

	private static final String GAME_MUSIC_TEXT = "BOSS MUSIC:\n\nHERO'S HEART! - BY HEATLYBROS";
	private static final float GAME_MUSIC_TEXT_X = MENU_MUSIC_TEXT_X;
	private static final float GAME_MUSIC_TEXT_Y = MENU_MUSIC_TEXT_Y - 50f;

	private static final String BACK_BUTTON_TEXT = "BACK";
	private static final float BACK_BUTTON_X = CAMERA_WIDTH / 2f;
	private static final float BACK_BUTTON_Y = 50;

	private TextUI menuMusicText;
	private TextUI gameMusicText;

	private ButtonUI backButton;
	private TextUI backButtonText;

	public CreditsScreen(UntitledGame game) {
		super(game);

		Assets A = game.getAssets();
		Viewport viewport = game.getViewport();
		InputMultiplexer multiplexer = game.getInputMultiplexer();

		this.menuMusicText = new TextUI(TOP_LEFT, A.getFont(MINECRAFT_8))
				.setX(MENU_MUSIC_TEXT_X)
				.setY(MENU_MUSIC_TEXT_Y)
				.setText(MENU_MUSIC_TEXT);

		this.gameMusicText = new TextUI(TOP_LEFT, A.getFont(MINECRAFT_8))
				.setX(GAME_MUSIC_TEXT_X)
				.setY(GAME_MUSIC_TEXT_Y)
				.setText(GAME_MUSIC_TEXT);

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
	public void update() {

	}

	@Override
	public void render(SpriteBatch batch) {
		this.menuMusicText.render(batch);
		this.gameMusicText.render(batch);

		this.backButton.render(batch);
		this.backButtonText.render(batch);
	}

	@Override
	public void renderDebug(ShapeRenderer renderer) {

	}
}
