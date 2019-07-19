package com.mygdx.game.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.UntitledGame;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.assets.MusicName;
import com.mygdx.game.highscores.Highscore;
import com.mygdx.game.screens.game.Background;
import com.mygdx.game.ui.ButtonUI;
import com.mygdx.game.ui.TextUI;
import com.mygdx.game.ui.TextureUI;

import static com.mygdx.game.UntitledGame.BUTTON_H;
import static com.mygdx.game.UntitledGame.BUTTON_W;
import static com.mygdx.game.UntitledGame.CAMERA_HEIGHT;
import static com.mygdx.game.UntitledGame.CAMERA_WIDTH;
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
	private static final int HIGHSCORES_LIMIT = 12;

	private static final float HIGHSCORES_ID_W = 30f;

	private static final String HIGHSCORES_NAME_TEXT = "NAME";
	private static final float HIGHSCORES_NAME_W = 70f;

	private static final String HIGHSCORES_LEVEL_TEXT = "LEVEL";
	private static final float HIGHSCORES_LEVEL_W = 70f;

	private static final String HIGHSCORES_TIME_TEXT = "TIME";
	private static final float HIGHSCORES_TIME_W = 100f;

	private static final String HIGHSCORES_SCORE_TEXT = "SCORE";
	private static final float HIGHSCORES_SCORE_W = 100f;

	private static final float HIGHSCORES_W = HIGHSCORES_ID_W + HIGHSCORES_NAME_W + HIGHSCORES_LEVEL_W + HIGHSCORES_SCORE_W + HIGHSCORES_TIME_W;
	private static final float HIGHSCORES_H = 16f;
	private static final float HIGHSCORES_X = (CAMERA_WIDTH - HIGHSCORES_W) / 2f;
	private static final float HIGHSCORES_Y = CAMERA_HEIGHT - 50f;

	private static final String LOADING_TEXT = "RETRIEVING HIGHSCORES...";
	private static final float LOADING_X = CAMERA_WIDTH / 2f;
	private static final float LOADING_Y = CAMERA_HEIGHT - 100f;

	private static final String HIGHSCORES_FAILED_TEXT = "FAILED TO RETRIEVE HIGHSCORES.";

	private static final String BACK_BUTTON_TEXT = "BACK";
	private static final float BACK_BUTTON_X = CAMERA_WIDTH / 2f;
	private static final float BACK_BUTTON_Y = 50;

	private Assets A;

	private Background background;
	private TextUI backText;
	private ButtonUI backButton;
	private TextUI loadingText;

	private boolean loading;
	private Array<HighscoreUI> highscoresUI;

	private class HighscoreUI {
		TextUI idText;
		TextUI nameText;
		TextUI levelText;
		TextUI scoreText;
		TextUI timeText;
		TextureUI background;

		HighscoreUI(float x, float y, BitmapFont font, Texture background) {
			this.idText = new TextUI(MIDDLE, font)
					.setX(x + HIGHSCORES_ID_W / 2f)
					.setY(y);

			this.nameText = new TextUI(MIDDLE, font)
					.setX(x + HIGHSCORES_ID_W + HIGHSCORES_NAME_W / 2f)
					.setY(y);

			this.levelText = new TextUI(MIDDLE, font)
					.setX(x + HIGHSCORES_ID_W + HIGHSCORES_NAME_W + HIGHSCORES_LEVEL_W / 2f)
					.setY(y);

			this.timeText = new TextUI(MIDDLE, font)
					.setX(x + HIGHSCORES_ID_W + HIGHSCORES_NAME_W + HIGHSCORES_LEVEL_W + HIGHSCORES_TIME_W / 2f)
					.setY(y);

			this.scoreText = new TextUI(MIDDLE, font)
					.setX(x + HIGHSCORES_ID_W + HIGHSCORES_NAME_W + HIGHSCORES_LEVEL_W + HIGHSCORES_TIME_W + HIGHSCORES_SCORE_W / 2f)
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
			this.levelText.render(batch);
			this.timeText.render(batch);
			this.scoreText.render(batch);
		}
	}

	public HighscoresScreen(UntitledGame game) {
		super(game);
		Viewport viewport = game.getViewport();
		InputMultiplexer multiplexer = game.getInputMultiplexer();

		this.A = game.getAssets();
		this.loading = true;
		this.background = new Background(A);

		this.backText = new TextUI(MIDDLE, A.getFont(MINECRAFT_8))
				.setX(BACK_BUTTON_X)
				.setY(BACK_BUTTON_Y)
				.setText(BACK_BUTTON_TEXT);

		this.backButton = new ButtonUI(MIDDLE, viewport, () -> setScreen(MAIN_MENU))
				.setX(BACK_BUTTON_X)
				.setY(BACK_BUTTON_Y)
				.setW(BUTTON_W)
				.setH(BUTTON_H)
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
				highscoreUI.levelText.setText(HIGHSCORES_LEVEL_TEXT);
				highscoreUI.timeText.setText(HIGHSCORES_TIME_TEXT);
				highscoreUI.scoreText.setText(HIGHSCORES_SCORE_TEXT);
			}

			highscoresUI.add(highscoreUI);
		}

		// Http request highscores
		game.getHighscores().getHighscores(HIGHSCORES_LIMIT, highscores -> {
			if (highscores == null) {
				loadingText.setText(HIGHSCORES_FAILED_TEXT);

			} else {
				for (int i = 0; i < highscores.size; i++) {
					Highscore highscore = highscores.get(i);
					HighscoreUI ui = highscoresUI.get(i + 1);

					ui.idText.setText(i + 1 + ".");
					ui.nameText.setText(highscore.getName());
					ui.levelText.setText(UntitledGame.formatLevel(highscore.getLevel()));
					ui.timeText.setText(UntitledGame.formatTime(highscore.getTime()));
					ui.scoreText.setText(String.valueOf(highscore.getScore()));
				}

				loading = false;
			}
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
	public void pauseScreen() {
		A.getMusic(MusicName.MAIN_MENU).pause();
	}

	@Override
	public void resumeScreen() {
		A.getMusic(MusicName.MAIN_MENU).play();
	}

	@Override
	public void renderDebug(ShapeRenderer renderer) {

	}
}
