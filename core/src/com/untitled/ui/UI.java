package com.untitled.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A User Interface object.
 * Has a (x, y) origin, and alignment of the content relative to the origin.
 */
public abstract class UI {
	/**
	 * x coordinate of the origin
	 */
	public float x;

	/**
	 * y coordinate of the origin
	 */
	public float y;

	private float w;
	private float h;
	private UIAlign align;

	/**
	 * @param align where the origin (x, y) of the UI should be relative to the content.
	 */
	public UI(UIAlign align) {
		this.align = align;
	}

	protected UI setX(float x) {
		this.x = x;
		return this;
	}

	protected UI setY(float y) {
		this.y = y;
		return this;
	}

	protected UI setW(float w) {
		this.w = w;
		return this;
	}

	protected UI setH(float h) {
		this.h = h;
		return this;
	}

	protected float getX() {
		switch (align) {
			case TOP_MIDDLE:
			case MIDDLE:
			case BOTTOM_MIDDLE:
				return x - w / 2;
			case TOP_RIGHT:
			case RIGHT:
			case BOTTOM_RIGHT:
				return x - w;
			default:
				return x;
		}
	}

	protected float getY() {
		switch (align) {
			case TOP_LEFT:
			case TOP_MIDDLE:
			case TOP_RIGHT:
				return y - h;
			case LEFT:
			case MIDDLE:
			case RIGHT:
				return y - h / 2;
			default:
				return y;
		}
	}

	float getW() {
		return w;
	}

	float getH() {
		return h;
	}

	/**
	 * Renders the UI
	 *
	 * @param batch {@link SpriteBatch} to render the UI on.
	 */
	public abstract void render(SpriteBatch batch);
}
