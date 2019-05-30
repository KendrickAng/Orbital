package com.mygdx.game.shape;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Rectangle {
	private float x;
	private float y;
	private float width;
	private float height;

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	// Check if point is within rectangle
	public boolean hitTest(Point point) {
		return x < point.getX() && point.getX() < x + width
				&& y < point.getY() && point.getY() < y + height;
	}

	// Check if either 4 corners are within rectangle
	public boolean hitTest(Rectangle rectangle) {
		return rectangle.hitTest(new Point(x, y))
				|| rectangle.hitTest(new Point(x + width, y))
				|| rectangle.hitTest(new Point(x, y + height))
				|| rectangle.hitTest(new Point(x + width, y + height));
	}

	public void renderDebug(ShapeRenderer shapeRenderer) {
		shapeRenderer.rect(x, y, width, height);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
}
