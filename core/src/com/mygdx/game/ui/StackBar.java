package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.screens.game.character.Assassin;

public class StackBar extends UI {
	private static final int BORDER = 2;
	private Assassin assassin;
	private Texture foreground;
	private Texture background;

	public StackBar(UIAlign align, Assassin assassin, Texture texture, Texture background) {
		super(align);
		this.assassin = assassin;
		this.foreground = texture;
		this.background = background;
		setH(background.getHeight());
	}

	@Override
	public StackBar setX(float x) {
		super.setX(x);
		return this;
	}

	@Override
	public StackBar setY(float y) {
		super.setY(y);
		return this;
	}

	@Override
	public StackBar setW(float w) {
		super.setW(w);
		return this;
	}

	public void render(SpriteBatch batch) {
		float width = (float) assassin.getStacks() / assassin.getMaxStacks() * getW();
		if (width < 0) {
			width = 0;
		}

		batch.draw(background, getX(), getY(), getW(), getH());
		batch.draw(foreground, getX(), getY() + BORDER, width, foreground.getHeight());
	}
}
