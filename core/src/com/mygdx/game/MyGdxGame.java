package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.texture.TextureManager;

public class MyGdxGame extends Game {
	// define game variables
	public static final int GAME_WIDTH = 600;
	public static final int GAME_HEIGHT = 380;
	public static final int MAP_HEIGHT = GAME_HEIGHT / 9; // accounts for ground
	public static final boolean DEBUG = true;

	// TODO: Tile the background.
	private SpriteBatch batch;
	private BitmapFont font;
	private OrthographicCamera camera;
	private Viewport viewport;
	private TextureManager textureManager;

	@Override
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);
		viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera);
		textureManager = new TextureManager();
		textureManager.loadTextures(); // load textures

		Gdx.gl.glClearColor(0, 0, 0, 1);
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
		textureManager.dispose();
	}

	public SpriteBatch getSpriteBatch() {
		return batch;
	}

	public BitmapFont getFont() {
		return font;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public Viewport getViewport() {
		return viewport;
	}

	public TextureManager getTextureManager() {
		return this.textureManager;
	}
}
