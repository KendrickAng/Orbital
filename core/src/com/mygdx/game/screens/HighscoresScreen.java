package com.mygdx.game.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Background;
import com.mygdx.game.UntitledGame;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.net.GetRequest;
import com.mygdx.game.ui.ButtonUI;
import com.mygdx.game.ui.TextUI;

import static com.mygdx.game.UntitledGame.BUTTON_HEIGHT;
import static com.mygdx.game.UntitledGame.BUTTON_WIDTH;
import static com.mygdx.game.UntitledGame.WINDOW_HEIGHT;
import static com.mygdx.game.UntitledGame.WINDOW_WIDTH;
import static com.mygdx.game.assets.FontName.MINECRAFT_16;
import static com.mygdx.game.assets.FontName.MINECRAFT_8;
import static com.mygdx.game.assets.TextureName.BUTTON_HOVER;
import static com.mygdx.game.screens.ScreenName.MAIN_MENU;
import static com.mygdx.game.ui.UIAlign.MIDDLE;

public class HighscoresScreen extends UntitledScreen {
	private static final int HIGHSCORES = 10;
	private static final float HIGHSCORES_NAME_X = WINDOW_WIDTH / 2f - 50f;
	private static final float HIGHSCORES_SCORE_X = WINDOW_WIDTH / 2f + 50f;
	private static final float HIGHSCORES_Y = WINDOW_HEIGHT - 50f;
	private static final float HIGHSCORES_H = 16f;

	private static final String LOADING_TEXT = "LOADING...";
	private static final float LOADING_X = WINDOW_WIDTH / 2f;
	private static final float LOADING_Y = WINDOW_HEIGHT - 100f;

	private static final String WEB_API_KEY = "AIzaSyCqpJqKdS-fbgIKlyZ5uMqsg-1JCk8zMBQ";
	private static final String NEW_USER_URL = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/signupNewUser";
	private static final String HIGHSCORES_URL = "https://firestore.googleapis.com/v1/projects/apollo1orbital3/databases/(default)/documents/highscores";

	private static final String BACK_BUTTON_TEXT = "BACK";
	private static final float BACK_BUTTON_X = WINDOW_WIDTH / 2f;
	private static final float BACK_BUTTON_Y = 50;

	private Background background;
	private TextUI backText;
	private ButtonUI backButton;
	private TextUI loadingText;

	private Array<Highscore> highscores;

	private class Highscore {
		TextUI nameText;
		TextUI scoreText;

		Highscore(Assets A, int i) {
			nameText = new TextUI(MIDDLE, A.getFont(MINECRAFT_8))
					.setX(HIGHSCORES_NAME_X)
					.setY(HIGHSCORES_Y - HIGHSCORES_H * i);

			scoreText = new TextUI(MIDDLE, A.getFont(MINECRAFT_8))
					.setX(HIGHSCORES_SCORE_X)
					.setY(HIGHSCORES_Y - HIGHSCORES_H * i);
		}
	}

	public HighscoresScreen(UntitledGame game) {
		super(game);
		Assets A = game.getAssets();
		Viewport viewport = game.getViewport();
		InputMultiplexer multiplexer = game.getInputMultiplexer();

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
				.setHoverTexture(A.getTexture(BUTTON_HOVER));

		this.loadingText = new TextUI(MIDDLE, A.getFont(MINECRAFT_8))
				.setX(LOADING_X)
				.setY(LOADING_Y)
				.setText(LOADING_TEXT);

		// Add input processors
		multiplexer.addProcessor(backButton);

		// Highscores table
		this.highscores = new Array<>();

		new GetRequest(HIGHSCORES_URL)
				.setSuccessCallback(response -> {
//					Gdx.app.log("Response", response);

					JsonReader jsonReader = new JsonReader();
					JsonValue json = jsonReader.parse(response);

					int i = 0;
					for (JsonValue document : json.get("documents")) {
						JsonValue fields = document.get("fields");
						String name = fields.get("name").getString("stringValue");
						int score = fields.get("score").getInt("integerValue");
//						Gdx.app.log(name, String.valueOf(score));

						Highscore highscore = new Highscore(A, i++);
						highscore.nameText.setText(name);
						highscore.scoreText.setText(String.valueOf(score));
						highscores.add(highscore);
					}
				})
				.call();
	}

	@Override
	public void update() {

	}

	@Override
	public void render(SpriteBatch batch) {
		this.background.render(batch);
		this.backButton.render(batch);
		this.backText.render(batch);

		if (highscores.isEmpty()) {
			loadingText.render(batch);
		} else {
			for (Highscore h : highscores) {
				h.nameText.render(batch);
				h.scoreText.render(batch);
			}
		}
	}

	@Override
	public void renderDebug(ShapeRenderer renderer) {

	}
}
