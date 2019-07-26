package com.untitled.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A {@link UI} which displays a Texture.
 */
public class TextureUI extends UI {
	private Texture texture;

	public TextureUI(UIAlign align, Texture texture) {
		super(align);
		this.texture = texture;
	}

	@Override
	public TextureUI setX(float x) {
		super.setX(x);
		return this;
	}

	@Override
	public TextureUI setY(float y) {
		super.setY(y);
		return this;
	}

	@Override
	public TextureUI setW(float w) {
		super.setW(w);
		return this;
	}

	@Override
	public TextureUI setH(float h) {
		super.setH(h);
		return this;
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(texture, getX(), getY(), getW(), getH());
	}
}
