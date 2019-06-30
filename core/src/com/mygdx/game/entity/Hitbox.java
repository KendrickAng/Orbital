package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Hitbox {
	private EntityData entityData;

	private int width;
	private int height;
	private int offsetX;
	private int offsetY;
	private int pixmapWidth;
	private int pixmapHeight;

	public Hitbox(EntityData entityData, Pixmap pixmap) {
		this.entityData = entityData;
		this.pixmapWidth = pixmap.getWidth();
		this.pixmapHeight = pixmap.getHeight();

		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		// loop through all pixels in pixmap and start minmaxX/minmaxY
		for (int y = 0; y < pixmapHeight; y++) {
			for (int x = 0; x < pixmapWidth; x++) {
				// check for transparent pixel.
				if ((pixmap.getPixel(x, y) & 0x000000ff) == 0x000000ff) {
					minX = Math.min(x, minX);
					maxX = Math.max(x, maxX);
					minY = Math.min(y, minY);
					maxY = Math.max(y, maxY);
				}
			}
		}

		offsetX = minX;
		offsetY = pixmapHeight - maxY;
		width = maxX - minX;
		height = maxY - minY;
	}

	// AABB collision
	public boolean hitTest(Hitbox hitbox) {
		return getX() < hitbox.getX() + hitbox.getWidth() &&
				getX() + getWidth() > hitbox.getX() &&
				getY() < hitbox.getY() + hitbox.getHeight() &&
				getY() + getHeight() > hitbox.getY();
	}

	public void renderDebug(ShapeRenderer shapeRenderer) {
		shapeRenderer.rect(getX(), getY(), width, height);
	}

	public float getX() {
		if (entityData.getFlipX().get()) {
			return entityData.getPosition().x + pixmapWidth - offsetX - width;
		} else {
			return entityData.getPosition().x + offsetX;
		}
	}

	public float getY() {
		return entityData.getPosition().y + offsetY;
	}

	public float getOffsetX() {
		return offsetX;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
}
