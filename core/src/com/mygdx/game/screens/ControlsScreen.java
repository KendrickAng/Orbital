package com.mygdx.game.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Background;
import com.mygdx.game.UntitledGame;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.ui.ButtonUI;
import com.mygdx.game.ui.TextUI;

import static com.mygdx.game.UntitledGame.BUTTON_HEIGHT;
import static com.mygdx.game.UntitledGame.BUTTON_WIDTH;
import static com.mygdx.game.UntitledGame.WINDOW_HEIGHT;
import static com.mygdx.game.UntitledGame.WINDOW_WIDTH;
import static com.mygdx.game.assets.FontName.MINECRAFT_16;
import static com.mygdx.game.assets.TextureName.BUTTON_HOVER;
import static com.mygdx.game.assets.TextureName.BUTTON_NORMAL;
import static com.mygdx.game.screens.ScreenName.MAIN_MENU;
import static com.mygdx.game.ui.UIAlign.MIDDLE;

public class ControlsScreen extends UntitledScreen {
	private static final String CONTROLS_TEXT = "Q - PRIMARY SKILL\nW - SECONDARY SKILL\nE - TERTIARY SKILL\nR - SWITCH CHARACTER (ASSASSIN/TANK)";
	private static final float CONTROLS_X = WINDOW_WIDTH / 2f;
	private static final float CONTROLS_Y = WINDOW_HEIGHT / 2f;

	private static final String BACK_BUTTON_TEXT = "BACK";
	private static final float BACK_BUTTON_X = WINDOW_WIDTH / 2f;
	private static final float BACK_BUTTON_Y = 50;

	private Background background;
	private TextUI controls;
	private TextUI backText;
	private ButtonUI backButton;

	public ControlsScreen(UntitledGame game) {
		super(game);
		Assets A = game.getAssets();
		Viewport viewport = game.getViewport();
		InputMultiplexer multiplexer = game.getInputMultiplexer();

		this.background = new Background(A);

		this.controls = new TextUI(MIDDLE, A.getFont(MINECRAFT_16))
				.setX(CONTROLS_X)
				.setY(CONTROLS_Y)
				.setText(CONTROLS_TEXT);

		this.backText = new TextUI(MIDDLE, A.getFont(MINECRAFT_16))
				.setX(BACK_BUTTON_X)
				.setY(BACK_BUTTON_Y)
				.setText(BACK_BUTTON_TEXT);

		this.backButton = new ButtonUI(MIDDLE, viewport, () -> setScreen(MAIN_MENU))
				.setX(BACK_BUTTON_X)
				.setY(BACK_BUTTON_Y)
				.setW(BUTTON_WIDTH)
				.setH(BUTTON_HEIGHT)
				.setNormalTexture(A.getTexture(BUTTON_NORMAL))
				.setHoverTexture(A.getTexture(BUTTON_HOVER));

		// Add input processors
		multiplexer.addProcessor(backButton);
	}

	@Override
	public void update() {

	}

	@Override
	public void render(SpriteBatch batch) {
		this.background.render(batch);
		this.controls.render(batch);
		this.backButton.render(batch);
		this.backText.render(batch);
	}

	@Override
	public void renderDebug(ShapeRenderer renderer) {

	}
}
