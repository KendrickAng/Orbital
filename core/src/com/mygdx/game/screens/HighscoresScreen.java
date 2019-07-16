package com.mygdx.game.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Background;
import com.mygdx.game.UntitledGame;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.highscores.Highscore;
import com.mygdx.game.ui.ButtonUI;
import com.mygdx.game.ui.TextUI;
import com.mygdx.game.ui.TextureUI;

import static com.mygdx.game.UntitledGame.BUTTON_HEIGHT;
import static com.mygdx.game.UntitledGame.BUTTON_WIDTH;
import static com.mygdx.game.UntitledGame.WINDOW_HEIGHT;
import static com.mygdx.game.UntitledGame.WINDOW_WIDTH;
import static com.mygdx.game.assets.FontName.MINECRAFT_16;
import static com.mygdx.game.assets.FontName.MINECRAFT_8;
import static com.mygdx.game.assets.TextureName.BUTTON_HOVER;
import static com.mygdx.game.assets.TextureName.BUTTON_NORMAL;
import static com.mygdx.game.assets.TextureName.HIGHSCORES_EVEN;
import static com.mygdx.game.assets.TextureName.HIGHSCORES_ODD;
import static com.mygdx.game.assets.TextureName.HIGHSCORES_TITLE;
import static com.mygdx.game.screens.ScreenName.MAIN_MENU;
import static com.mygdx.game.ui.UIAlign.LEFT;
import static com.mygdx.game.ui.UIAlign.MIDDLE;

public class HighscoresScreen extends UntitledScreen {
	private static final int HIGHSCORES_LIMIT = 10;

	private static final float HIGHSCORES_ID_W = 30f;

	private static final String HIGHSCORES_NAME_TEXT = "NAME";
	private static final float HIGHSCORES_NAME_W = 70f;

	private static final String HIGHSCORES_SCORE_TEXT = "SCORE";
	private static final float HIGHSCORES_SCORE_W = 100f;

	private static final float HIGHSCORES_W = HIGHSCORES_ID_W + HIGHSCORES_NAME_W + HIGHSCORES_SCORE_W;
	private static final float HIGHSCORES_H = 16f;
	private static final float HIGHSCORES_X = (WINDOW_WIDTH - HIGHSCORES_W) / 2f;
	private static final float HIGHSCORES_Y = WINDOW_HEIGHT - 50f;

	private static final String LOADING_TEXT = "RETRIEVING HIGHSCORES...";
	private static final float LOADING_X = WINDOW_WIDTH / 2f;
	private static final float LOADING_Y = WINDOW_HEIGHT - 100f;

	private static final String BACK_BUTTON_TEXT = "BACK";
	private static final float BACK_BUTTON_X = WINDOW_WIDTH / 2f;
	private static final float BACK_BUTTON_Y = 50;

	private Background background;
	private TextUI backText;
	private ButtonUI backButton;
	private TextUI loadingText;

	private boolean loading;
	private Array<HighscoreUI> highscoresUI;

	private class HighscoreUI {
		TextUI idText;
		TextUI nameText;
		TextUI scoreText;
		TextureUI background;

		HighscoreUI(float x, float y, BitmapFont font, Texture background) {
			this.idText = new TextUI(MIDDLE, font)
					.setX(x + HIGHSCORES_ID_W / 2f)
					.setY(y);

			this.nameText = new TextUI(MIDDLE, font)
					.setX(x + HIGHSCORES_ID_W + HIGHSCORES_NAME_W / 2f)
					.setY(y);

			this.scoreText = new TextUI(MIDDLE, font)
					.setX(x + HIGHSCORES_ID_W + HIGHSCORES_NAME_W + HIGHSCORES_SCORE_W / 2f)
					.setY(y);

			this.background = new TextureUI(LEFT, background)
					.setX(x)
					.setY(y)
					.setW(HIGHSCORES_W)
					.setH(HIGHSCORES_H);
		}

		void render(SpriteBatch batch) {
			this.background.render(batch);
			this.idText.render(batch);
			this.nameText.render(batch);
			this.scoreText.render(batch);
		}
	}

	public HighscoresScreen(UntitledGame game) {
		super(game);
		Assets A = game.getAssets();
		Viewport viewport = game.getViewport();
		InputMultiplexer multiplexer = game.getInputMultiplexer();

		this.loading = true;
		this.background = new Background(A);

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

		this.loadingText = new TextUI(MIDDLE, A.getFont(MINECRAFT_8))
				.setX(LOADING_X)
				.setY(LOADING_Y)
				.setText(LOADING_TEXT);

		// Add input processors
		multiplexer.addProcessor(backButton);

		// Highscores table
		this.highscoresUI = new Array<>();

		for (int i = 0; i < HIGHSCORES_LIMIT + 1; i++) {
			Texture texture;
			if (i == 0) {
				texture = A.getTexture(HIGHSCORES_TITLE);
			} else if (i % 2 == 0) {
				texture = A.getTexture(HIGHSCORES_EVEN);
			} else {
				texture = A.getTexture(HIGHSCORES_ODD);
			}

			HighscoreUI highscoreUI = new HighscoreUI(HIGHSCORES_X, HIGHSCORES_Y - HIGHSCORES_H * i
					, A.getFont(MINECRAFT_8), texture);

			if (i == 0) {
				highscoreUI.nameText.setText(HIGHSCORES_NAME_TEXT);
				highscoreUI.scoreText.setText(HIGHSCORES_SCORE_TEXT);
			}

			highscoresUI.add(highscoreUI);
		}

		// Http request highscores
		game.getHighscores().getHighscores(HIGHSCORES_LIMIT, highscores -> {
			for (int i = 0; i < highscores.size; i++) {
				Highscore highscore = highscores.get(i);
				HighscoreUI ui = highscoresUI.get(i + 1);

				ui.idText.setText(i + 1 + ".");
				ui.nameText.setText(highscore.getName());
				ui.scoreText.setText(String.valueOf(highscore.getScore()));
			}

			loading = false;
		});
	}

	@Override
	public void update() {

	}

	@Override
	public void render(SpriteBatch batch) {
		this.background.render(batch);
		this.backButton.render(batch);
		this.backText.render(batch);

		if (loading) {
			loadingText.render(batch);
		} else {
			for (HighscoreUI h : new Array.ArrayIterator<>(highscoresUI)) {
				h.render(batch);
			}
		}
	}

	@Override
	public void renderDebug(ShapeRenderer renderer) {

	}
}