package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.shape.Point;
import com.mygdx.game.texture.TextureManager;
import com.mygdx.game.ui.ColorTexture;
import com.mygdx.game.ui.ColorTextures;

public class MyGdxGame extends Game {
	// define game variables
	public static final int GAME_WIDTH = 600;
	public static final int GAME_HEIGHT = 380;
	public static final int MAP_HEIGHT = GAME_HEIGHT / 9; // accounts for ground
	public static final boolean DEBUG = true;

	private ColorTextures colorTextures;

	private SpriteBatch batch;
	private BitmapFont font;
	private OrthographicCamera camera;
	private Viewport viewport;
	private TextureManager textureManager;
	private InputMultiplexer inputMultiplexer;

	// TODO: AssetManager for faster loading

	@Override
	public void create() {
		colorTextures = new ColorTextures();
		batch = new SpriteBatch();
		font = new BitmapFont();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);
		viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera);
		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer); // set processor for input.
		textureManager = new TextureManager();
		textureManager.loadTextures(); // load textures.

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
		colorTextures.dispose();
	}

	public Point unproject(float x, float y) {
		Vector2 vector2 = new Vector2(x, y);
		viewport.unproject(vector2);
		return new Point(vector2.x, vector2.y);
	}

	/* GETTERS */
	public ColorTextures getColorTextures() { return this.colorTextures; }
	public SpriteBatch getSpriteBatch() { return batch; }
	public BitmapFont getFont() { return font; }
	public OrthographicCamera getCamera() { return camera; }
	public Viewport getViewport() { return viewport; }
	public InputMultiplexer getInputMultiplexer() { return this.inputMultiplexer; }
	public TextureManager getTextureManager() { return this.textureManager; }
}
