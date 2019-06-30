package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ui.Button;
import com.mygdx.game.ui.ColorTextures;
import com.mygdx.game.ui.Text;

import static com.mygdx.game.MyGdxGame.GAME_HEIGHT;
import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.ui.ColorTextures.ColorEntry.DEEP_PURPLE_500;
import static com.mygdx.game.ui.ContentAlignment.CENTER;

// TODO: Why does the exit button take so damn long to load? MEMORY HOGGING ???
public class SettingsScreen implements Screen {
    private static final String PLACEHOLDER_TEXT = "Q - PRIMARY SKILL\nW - SECONDARY SKILL\nE - TERTIARY SKILL\nR - SWITCH CHARACTER (ASSASSIN/TANK)";
    private static final int PLACEHOLDER_FONTSIZE = 20;
    private static final float TOP_PADDING = GAME_HEIGHT / 2f;

    private static final String EXIT_BUTTON_TITLE = "EXIT";
    private static final ColorTextures.ColorEntry EXIT_BUTTON_HOVER_COLOR = DEEP_PURPLE_500;
    private static final int EXIT_BUTTON_TEXTSIZE = 25;
    private static final int EXIT_BUTTON_WIDTH = GAME_WIDTH;
    private static final int EXIT_BUTTON_HEIGHT = 50;
    private static final int EXIT_BUTTON_PADDING_X = 0;
    private static final int EXIT_BUTTON_PADDING_Y = 5;
    private static final Vector2 EXIT_BUTTON_COORDS = new Vector2(GAME_WIDTH / 2f, EXIT_BUTTON_HEIGHT);

    private MyGdxGame game;
    private Text placeholder;
    private Button exitButton;

    public SettingsScreen(MyGdxGame game) {
        this.game = game;
        this.placeholder = new Text(PLACEHOLDER_TEXT, PLACEHOLDER_FONTSIZE, game)
                        .setX(GAME_WIDTH / 2f)
                        .setY(GAME_HEIGHT - TOP_PADDING)
                        .setAlignment(CENTER);
        this.exitButton = new Button(EXIT_BUTTON_TITLE, EXIT_BUTTON_TEXTSIZE, game)
                        .setHoverColor(EXIT_BUTTON_HOVER_COLOR)
                        .setX(EXIT_BUTTON_COORDS.x)
                        .setY(EXIT_BUTTON_COORDS.y)
                        .setAlignment(CENTER)
                        .setMinWidth(EXIT_BUTTON_WIDTH)
                        .setMinHeight(EXIT_BUTTON_HEIGHT)
                        .setPaddingX(EXIT_BUTTON_PADDING_X)
                        .setPaddingY(EXIT_BUTTON_PADDING_Y)
                        .setCallback(() -> {
                            game.setScreen(new MainMenuScreen(game));
                            game.getInputMultiplexer().removeProcessor(exitButton);
                            dispose();
                        });
        this.game.getInputMultiplexer().addProcessor(exitButton);
    }

    @Override
    public void show() {
        Gdx.app.log("SettingsScreen.java", "show() called");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        OrthographicCamera camera = game.getCamera();
        camera.update();

        SpriteBatch batch = game.getSpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        this.placeholder.render();
        this.exitButton.render();
        batch.end();

        // Add input processors
        InputMultiplexer multiplexer = game.getInputMultiplexer();
        multiplexer.addProcessor(exitButton);
    }

    @Override
    public void resize(int width, int height) {
        game.getViewport().update(width, height);
    }

    @Override
    public void pause() { }
    @Override
    public void resume() { }
    @Override
    public void hide() { }
    @Override
    public void dispose() { }
}
