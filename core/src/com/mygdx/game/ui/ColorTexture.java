package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.shape.Rectangle;

import java.util.Locale;

/**
 * Represents a rectangle filled with a texture, to be drawn only.
 */
public class ColorTexture {
	private MyGdxGame game;
	private Rectangle rectangle;
	private Texture texture;

	public ColorTexture(MyGdxGame game) {
		this.game = game;
	}

	public ColorTexture setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
		return this;
	}

	public ColorTexture setTexture(Texture texture) {
		this.texture = texture;
		return this;
	}

	public void render() {
		game.getSpriteBatch().draw(texture,
				rectangle.getX(),
				rectangle.getY(),
				rectangle.getWidth(),
				rectangle.getHeight());
	}

	@Override
	public String toString() {
		return String.format(Locale.ENGLISH, "%.2f %.2f %.2f %.2f",
				rectangle.getX(),
				rectangle.getY(),
				rectangle.getWidth(),
				rectangle.getHeight());
	}
}

