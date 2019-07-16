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
import static com.mygdx.game.UntitledGame.BUTTON_MENU_WIDTH;
import static com.mygdx.game.UntitledGame.WINDOW_HEIGHT;
import static com.mygdx.game.UntitledGame.WINDOW_WIDTH;
import static com.mygdx.game.assets.FontName.MINECRAFT_16;
import static com.mygdx.game.assets.FontName.MINECRAFT_32;
import static com.mygdx.game.assets.TextureName.BUTTON_MENU_HOVER;
import static com.mygdx.game.screens.ScreenName.CONTROLS;
import static com.mygdx.game.screens.ScreenName.GAME;
import static com.mygdx.game.screens.ScreenName.HIGHSCORES;
import static com.mygdx.game.ui.UIAlign.MIDDLE;
import static com.mygdx.game.ui.UIAlign.TOP_MIDDLE;

public class MainMenuScreen extends UntitledScreen {
	private static final String TITLE = "UNTITLED";
	private static final float TITLE_X = WINDOW_WIDTH / 2f;
	private static final float TITLE_Y = WINDOW_HEIGHT - 50;

	private static final String PLAY_BUTTON_TEXT = "PLAY";
	private static final float PLAY_BUTTON_X = WINDOW_WIDTH / 2f;
	private static final float PLAY_BUTTON_Y = WINDOW_HEIGHT / 2f;

	private static final String CONTROLS_BUTTON_TEXT = "CONTROLS";
	private static final float CONTROLS_BUTTON_X = WINDOW_WIDTH / 2f;
	private static final float CONTROLS_BUTTON_Y = PLAY_BUTTON_Y - BUTTON_HEIGHT;

	private static final String HIGHSCORES_BUTTON_TEXT = "HIGHSCORES";
	private static final float HIGHSCORES_BUTTON_X = WINDOW_WIDTH / 2f;
	private static final float HIGHSCORES_BUTTON_Y = CONTROLS_BUTTON_Y - BUTTON_HEIGHT;

	private Background background;

	private TextUI title;
	private TextUI playText;
	private ButtonUI playButton;
	private TextUI controlsText;
	private ButtonUI controlsButton;
	private TextUI highscoresText;
	private ButtonUI highscoresButton;

	public MainMenuScreen(UntitledGame game) {
		super(game);
		Assets A = game.getAssets();
		Viewport viewport = game.getViewport();
		InputMultiplexer multiplexer = game.getInputMultiplexer();

		this.background = new Background(A);

		this.title = new TextUI(TOP_MIDDLE, A.getFont(MINECRAFT_32))
				.setX(TITLE_X)
				.setY(TITLE_Y)
				.setText(TITLE);

		this.playText = new TextUI(MIDDLE, A.getFont(MINECRAFT_16))
				.setX(PLAY_BUTTON_X)
				.setY(PLAY_BUTTON_Y)
				.setText(PLAY_BUTTON_TEXT);

		this.playButton = new ButtonUI(MIDDLE, viewport, () -> setScreen(GAME))
				.setX(PLAY_BUTTON_X)
				.setY(PLAY_BUTTON_Y)
				.setW(BUTTON_MENU_WIDTH)
				.setH(BUTTON_HEIGHT)
				.setHoverTexture(A.getTexture(BUTTON_MENU_HOVER));

		this.controlsText = new TextUI(MIDDLE, A.getFont(MINECRAFT_16))
				.setX(CONTROLS_BUTTON_X)
				.setY(CONTROLS_BUTTON_Y)
				.setText(CONTROLS_BUTTON_TEXT);

		this.controlsButton = new ButtonUI(MIDDLE, viewport, () -> setScreen(CONTROLS))
				.setX(CONTROLS_BUTTON_X)
				.setY(CONTROLS_BUTTON_Y)
				.setW(BUTTON_MENU_WIDTH)
				.setH(BUTTON_HEIGHT)
				.setHoverTexture(A.getTexture(BUTTON_MENU_HOVER));

		this.highscoresText = new TextUI(MIDDLE, A.getFont(MINECRAFT_16))
				.setX(HIGHSCORES_BUTTON_X)
				.setY(HIGHSCORES_BUTTON_Y)
				.setText(HIGHSCORES_BUTTON_TEXT);

		this.highscoresButton = new ButtonUI(MIDDLE, viewport, () -> setScreen(HIGHSCORES))
				.setX(HIGHSCORES_BUTTON_X)
				.setY(HIGHSCORES_BUTTON_Y)
				.setW(BUTTON_MENU_WIDTH)
				.setH(BUTTON_HEIGHT)
				.setHoverTexture(A.getTexture(BUTTON_MENU_HOVER));

		// Add input processors
		multiplexer.addProcessor(playButton);
		multiplexer.addProcessor(controlsButton);
		multiplexer.addProcessor(highscoresButton);
	}

	@Override
	public void update() {

	}

	@Override
	public void render(SpriteBatch batch) {
		this.background.render(batch);
		this.title.render(batch);
		this.playButton.render(batch);
		this.playText.render(batch);
		this.controlsButton.render(batch);
		this.controlsText.render(batch);
		this.highscoresButton.render(batch);
		this.highscoresText.render(batch);
	}

	@Override
	public void renderDebug(ShapeRenderer renderer) {

	}
}
