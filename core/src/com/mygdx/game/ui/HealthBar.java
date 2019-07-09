package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.entity.LivingEntity;

public class HealthBar {
	private static final int BORDER = 2;
	private LivingEntity entity;
	private Texture foreground;
	private Texture background;

	private float x;
	private float y;
	private float width;

	public HealthBar(LivingEntity entity, Texture texture, Texture background) {
		this.entity = entity;
		this.foreground = texture;
		this.background = background;
	}

	public HealthBar setX(float x) {
		this.x = x;
		return this;
	}

	public HealthBar setY(float y) {
		this.y = y;
		return this;
	}

	public HealthBar setWidth(float width) {
		this.width = width;
		return this;
	}

	public void render(SpriteBatch batch) {
		float width = entity.getHealth() / entity.getMaxHealth() * this.width;
		if (width < 0) {
			width = 0;
		}

		batch.draw(background, x, y - BORDER, this.width, background.getHeight());
		batch.draw(foreground, x, y, width, foreground.getHeight());
	}
}
