package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Background;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.ui.Button;
import com.mygdx.game.ui.Text;

import static com.mygdx.game.MyGdxGame.GAME_HEIGHT;
import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.ui.ColorTextures.ColorEntry;
import static com.mygdx.game.ui.ColorTextures.ColorEntry.DEEP_PURPLE_500;
import static com.mygdx.game.ui.ContentAlignment.CENTER;

public class MainMenuScreen implements Screen {
	private static final String TITLE = "UNTITLED";
	private static final int TITLE_FONTSIZE = 35;
	private static final int TOP_PADDING = 50;

	private static final String PLAY_BUTTON_TITLE = "PLAY";
	private static final ColorEntry PLAY_BUTTON_HOVER_COLOR = DEEP_PURPLE_500;
	private static final int PLAY_BUTTON_TEXTSIZE = 25;
	private static final int PLAY_BUTTON_WIDTH = GAME_WIDTH;
	private static final int PLAY_BUTTON_HEIGHT = 50;
	private static final int PLAY_BUTTON_PADDING_X = 0;
	private static final int PLAY_BUTTON_PADDING_Y = 5;
	private static final Vector2 PLAY_BUTTON_COORDS = new Vector2(GAME_WIDTH / 2f, GAME_HEIGHT / 2f);

	private static final String SETTINGS_BUTTON_TITLE = "CONTROLS";
	private static final ColorEntry SETTINGS_BUTTON_HOVER_COLOR = DEEP_PURPLE_500;
	private static final int SETTINGS_BUTTON_TEXTSIZE = 25;
	private static final int SETTINGS_BUTTON_WIDTH = GAME_WIDTH;
	private static final int SETTINGS_BUTTON_HEIGHT = 50;
	private static final int SETTINGS_BUTTON_PADDING_X = 0;
	private static final int SETTINGS_BUTTON_PADDING_Y = 5;
	private static final Vector2 SETTINGS_BUTTON_COORDS = new Vector2(GAME_WIDTH / 2f, GAME_HEIGHT / 2f - PLAY_BUTTON_HEIGHT);

	private MyGdxGame game;
	private Background background;
	private Text title;
	private Button playButton;
	private Button settingsButton;

	public MainMenuScreen(MyGdxGame game) {
		this.game = game;
		this.background = new Background(game.getAssets());
		this.title = new Text(TITLE, TITLE_FONTSIZE, game)
				.setX(GAME_WIDTH / 2f)
				.setY(GAME_HEIGHT - TOP_PADDING)
				.setAlignment(CENTER);
		this.playButton = new Button(PLAY_BUTTON_TITLE, PLAY_BUTTON_TEXTSIZE, game)
				.setHoverColor(PLAY_BUTTON_HOVER_COLOR)
				.setX(PLAY_BUTTON_COORDS.x)
				.setY(PLAY_BUTTON_COORDS.y)
				.setAlignment(CENTER)
				.setMinWidth(PLAY_BUTTON_WIDTH)
				.setMinHeight(PLAY_BUTTON_HEIGHT)
				.setPaddingX(PLAY_BUTTON_PADDING_X)
				.setPaddingY(PLAY_BUTTON_PADDING_Y)
				.setCallback(() -> {
					game.getInputMultiplexer().removeProcessor(playButton);
					game.getInputMultiplexer().removeProcessor(settingsButton);
					game.setScreen(new GameScreen(game));
					dispose();
				});
		this.settingsButton = new Button(SETTINGS_BUTTON_TITLE, SETTINGS_BUTTON_TEXTSIZE, game)
				.setHoverColor(SETTINGS_BUTTON_HOVER_COLOR)
				.setX(SETTINGS_BUTTON_COORDS.x)
				.setY(SETTINGS_BUTTON_COORDS.y)
				.setAlignment(CENTER)
				.setMinWidth(SETTINGS_BUTTON_WIDTH)
				.setMinHeight(SETTINGS_BUTTON_HEIGHT)
				.setPaddingX(SETTINGS_BUTTON_PADDING_X)
				.setPaddingY(SETTINGS_BUTTON_PADDING_Y)
				.setCallback(() -> {
					game.getInputMultiplexer().removeProcessor(playButton);
					game.getInputMultiplexer().removeProcessor(settingsButton);
					game.setScreen(new SettingsScreen(game));
					dispose();
				});
		// Add input processors
		InputMultiplexer multiplexer = game.getInputMultiplexer();
		multiplexer.addProcessor(playButton);
		multiplexer.addProcessor(settingsButton);
	}

	@Override
	public void show() {
		Gdx.app.log("MainMenuScreen.java", "show() called");
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		OrthographicCamera camera = game.getCamera();
		camera.update();

		SpriteBatch batch = game.getSpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		this.background.render(batch);
		this.title.render();
		this.playButton.render();
		this.settingsButton.render();
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		game.getViewport().update(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
	}
}
