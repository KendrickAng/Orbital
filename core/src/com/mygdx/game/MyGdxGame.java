package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends Game {
	// define game variables
	public static final int GAME_WIDTH = 1200;
	public static final int GAME_HEIGHT = 760;
	public static final int PLAYER_WIDTH = 25;
	public static final int PLAYER_HEIGHT = 50;
	protected static final int SENSITIVITY = 250;
	protected static final int MAP_HEIGHT = GAME_HEIGHT / 5; // accounts for ground

	protected SpriteBatch batch;
	protected BitmapFont font;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
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
}
