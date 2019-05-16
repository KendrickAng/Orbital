package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.MyGdxGame.SENSITIVITY;

public class GameScreen implements Screen {
    private Character player;
    private Background background;
    private MyGdxGame game;

    public GameScreen(MyGdxGame game) {
        // init rectangle to (0, 0)
        this.player = new Tank();
        this.background = new Background();
        this.game = game;
    }

    @Override
    public void show() {
        Gdx.app.log("GameScreen.java", "show() called");
    }

    // TODO: Abstract out the renderable stuff (background, rectangle) into array
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        player.render();
        background.render();
        int x = player.getX();
        int y = player.getY();

        // movement input handling, ensure player stays in bounds
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && x > 0) {
            player = player.move((int) (x - (SENSITIVITY * delta)), y);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && x < GAME_WIDTH - player.getWidth()) {
            player = player.move((int) (x + (SENSITIVITY * delta)), y);
        }

        // player input handling, activate skills on keypress
        if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
            player.primary();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            player.secondary();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.E)) {
            player.tertiary();
        }
    }

    @Override
    public void resize(int width, int height) {
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
        player.dispose();
        background.dispose();
    }
}
