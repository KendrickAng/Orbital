package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.shape.Rectangle;

public class Hitbox {
	private Vector2 position;

	private int x;
	private int y;
	private int width;
	private int height;
	private Rectangle hitbox;

	private boolean flipX;

	public Hitbox(Pixmap pixmap) {
		this.width = pixmap.getWidth();
		this.height = pixmap.getHeight();
		this.hitbox = new Rectangle();

		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if ((pixmap.getPixel(x, y) & 0x000000ff) == 0x000000ff) {
					minX = Math.min(x, minX);
					maxX = Math.max(x, maxX);
					minY = Math.min(y, minY);
					maxY = Math.max(y, maxY);
				}
			}
		}

		this.x = minX;
		this.y = height - maxY;
		hitbox.setWidth(maxX - minX);
		hitbox.setHeight(maxY - minY);
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public void setFlip(boolean flipX, boolean flipY) {
		this.flipX = flipX;
	}

	public boolean hitTest(Hitbox hitbox) {
		updateHitbox();
		hitbox.updateHitbox();
		return this.hitbox.hitTest(hitbox.hitbox);
	}

	public void renderDebug(ShapeRenderer shapeRenderer) {
		updateHitbox();
		hitbox.renderDebug(shapeRenderer);
	}

	private void updateHitbox() {
		if (flipX) {
			hitbox.setX(position.x + width - x - hitbox.getWidth());
		} else {
			hitbox.setX(position.x + x);
		}
		hitbox.setY(position.y + y);
	}

	public float getOffsetX() {
		return x;
	}

	public float getX() {
		updateHitbox();
		return hitbox.getX();
	}

	public float getY() {
		updateHitbox();
		return hitbox.getY();
	}

	public float getWidth() {
		return hitbox.getWidth();
	}

	public float getHeight() {
		return hitbox.getHeight();
	}
}
