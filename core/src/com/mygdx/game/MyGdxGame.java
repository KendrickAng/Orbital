package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MyGdxGame extends Game {
	// define game variables
	public static final int GAME_WIDTH = 1200;
	public static final int GAME_HEIGHT = 760;
	protected static final int SENSITIVITY = 250;
	protected static final int MAP_HEIGHT = GAME_HEIGHT / 5; // accounts for ground

	// TODO: Install LibGDX plugin for IntelliJ, you can see the warnings below.
	private static SpriteBatch batch;
	private static BitmapFont font;
	private static OrthographicCamera camera;
	private static Viewport viewport;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);
		viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera);
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}

	public static SpriteBatch getSpriteBatch() { return batch; }
	public static BitmapFont getFont() { return font; }
	public static OrthographicCamera getCamera() { return camera; }
	public static Viewport getViewport() { return viewport; }
}
