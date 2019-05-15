package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import static com.mygdx.game.MyGdxGame.SENSITIVITY;

public class GameScreen implements Screen {
    private Rectangle img;
    private MyGdxGame game;

    public GameScreen(MyGdxGame game) {
        // init rectangle to (0, 0)
        this.img = new Rectangle();
        this.game = game;
    }

    @Override
    public void show() {
        Gdx.app.log("GameScreen.java", "show() called");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        img.render();
        int x = img.getX();
        int y = img.getY();
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            img = img.move(x, y + SENSITIVITY);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            img = img.move(x, y - SENSITIVITY);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            img = img.move(x - SENSITIVITY, y);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            img = img.move(x + SENSITIVITY, y);
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

    }
}
