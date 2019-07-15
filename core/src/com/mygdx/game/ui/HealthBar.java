package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.entity.LivingEntity;

public class HealthBar extends UI {
	private static final int BORDER = 2;
	private LivingEntity entity;
	private Texture foreground;
	private Texture background;

	public HealthBar(UIAlign align, LivingEntity entity, Texture texture, Texture background) {
		super(align);
		this.entity = entity;
		this.foreground = texture;
		this.background = background;
		setH(background.getHeight());
	}

	@Override
	public HealthBar setX(float x) {
		super.setX(x);
		return this;
	}

	@Override
	public HealthBar setY(float y) {
		super.setY(y);
		return this;
	}

	@Override
	public HealthBar setW(float w) {
		super.setW(w);
		return this;
	}

	public void render(SpriteBatch batch) {
		float width = entity.getHealth() / entity.getMaxHealth() * getW();
		if (width < 0) {
			width = 0;
		}

		batch.draw(background, getX(), getY(), getW(), getH());
		batch.draw(foreground, getX(), getY() + BORDER, width, foreground.getHeight());
	}
}
