package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import static com.mygdx.game.CharacterType.ASSASSIN;
import static com.mygdx.game.MyGdxGame.getSpriteBatch;

public class GameScreen implements Screen {
    // Game reference.
    private MyGdxGame game;

    // For debugging.
    private ShapeRenderer shapeBatch;

    private CharacterController controller;
    private Background background;
    protected static EntityManager entityManager; // has to be accessed from character class

    public GameScreen(MyGdxGame game) {
        // init rectangle to (0, 0)
        this.controller = new CharacterController();
        controller.setCharacter(ASSASSIN);

        this.background = new Background();
        this.entityManager = new EntityManager();
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

        // TODO: ShapeRenderer should be for debugging purposes only.
        /*
         ShapeRenderer uses it's own batch rendering system.
         Which is why I have to seperate it from the main sprite batch or it can cause graphical issues.
          */
        shapeBatch.begin(ShapeRenderer.ShapeType.Filled);
        background.renderShape(shapeBatch);
        shapeBatch.end();

        // render hitboxes.
        shapeBatch.begin(ShapeRenderer.ShapeType.Line);
        controller.character().renderDebug(shapeBatch);
        shapeBatch.end();

        // render interactive entities (shuriken)
        getSpriteBatch().begin();
        entityManager.renderAll(getSpriteBatch());
        getSpriteBatch().end();

        // TODO: Don't use static spriteBatch. Use game reference.
        getSpriteBatch().begin();
        controller.character().render(getSpriteBatch());
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
