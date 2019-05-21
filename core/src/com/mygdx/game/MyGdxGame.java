package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MyGdxGame extends Game {
	// define game variables
	public static final int GAME_WIDTH = 600;
	public static final int GAME_HEIGHT = 380;
	protected static final int SENSITIVITY = 250;
	protected static final int MAP_HEIGHT = GAME_HEIGHT / 9; // accounts for ground
	protected static final int GRAVITATIONAL_ACC = -2;

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
