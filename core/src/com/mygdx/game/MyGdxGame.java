package com.mygdx.game;

import com.badlogic.gdx.Game;
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
	protected static final int MOVESPEED = 5;
	protected static final int GRAVITY = -2;
	protected static final int MAP_HEIGHT = GAME_HEIGHT / 9; // accounts for ground

	// TODO: Install LibGDX plugin for IntelliJ, you can see the warnings below.
	private SpriteBatch batch;
	private BitmapFont font;
	private OrthographicCamera camera;
	private Viewport viewport;
	private EntityManager entityManager; // stores interactive stuff like shurikens
	private TextureManager textureManager;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, GAME_WIDTH, GAME_HEIGHT);
		viewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT, camera);
		entityManager = new EntityManager();
		textureManager = new TextureManager();
		textureManager.loadTextures(); // load textures

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
		textureManager.dispose();
	}

	public SpriteBatch getSpriteBatch() { return batch; }
	public BitmapFont getFont() { return font; }
	public OrthographicCamera getCamera() { return camera; }
	public Viewport getViewport() { return viewport; }
	public EntityManager getEntityManager() { return this.entityManager; }
	public TextureManager getTextureManager() { return this.textureManager; }
}
