package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import static com.mygdx.game.MyGdxGame.GAME_WIDTH;
import static com.mygdx.game.MyGdxGame.SENSITIVITY;
import static com.mygdx.game.MyGdxGame.getSpriteBatch;

public class GameScreen implements Screen {
    // Game reference.
    private MyGdxGame game;

    // For debugging.
    private ShapeRenderer shapeBatch;

    private Character player;
    private Background background;

    public GameScreen(MyGdxGame game) {
        // init rectangle to (0, 0)
        this.player = new Assassin();
        this.background = new Background();
        this.shapeBatch = new ShapeRenderer();
        this.game = game;
    }

    @Override
    public void show() {
        Gdx.app.log("GameScreen.java", "show() called");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        MyGdxGame.getCamera().update();

        int x = player.getX();
        int y = player.getY();

        // TODO: Move to CharacterController class.
        // Character controller is the interface between the player (me) and the person I can control.
        /*
        has-a inputprocessor
        setCharacter() for now
         */
        // movement input handling, ensure player stays in bounds
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && x > 0) {
            player.move((int) (x - (SENSITIVITY * delta)), y);
            player.setDirection(Direction.LEFT);
        }
        // move this x checking in to the character class
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && x < GAME_WIDTH - player.getWidth()) {
            player.move((int) (x + (SENSITIVITY * delta)), y);
            player.setDirection(Direction.RIGHT);
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

        // TODO: ShapeRenderer should be for debugging purposes only.
        /*
         ShapeRenderer uses it's own batch rendering system.
         Which is why I have to seperate it from the main sprite batch or it can cause graphical issues.
          */
        shapeBatch.begin(ShapeRenderer.ShapeType.Filled);
        background.renderShape(shapeBatch);
        shapeBatch.end();

        shapeBatch.begin(ShapeRenderer.ShapeType.Line);
        player.renderShape(shapeBatch);
        shapeBatch.end();

        // TODO: Don't use static spriteBatch. Use game reference.
        getSpriteBatch().begin();
        player.render(getSpriteBatch());
        getSpriteBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        MyGdxGame.getViewport().update(width, height);
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
        shapeBatch.dispose();
    }
}
