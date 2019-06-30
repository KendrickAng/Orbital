package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.shape.Rectangle;

public class Hitbox {
	private EntityData entityData;

	private int x; // offsetX
	private int y;
	private int width; // pixmap width
	private int height;
	private Rectangle hitbox; // contain width and height of sprite

	public Hitbox(EntityData entityData, Pixmap pixmap) {
		this.entityData = entityData;
		this.width = pixmap.getWidth();
		this.height = pixmap.getHeight();
		this.hitbox = new Rectangle();

		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		// loop through all pixels in pixmap and start minmaxX/minmaxY
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// check for transparent pixel.
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

	public boolean hitTest(Hitbox hitbox) {
		updateHitbox();
		hitbox.updateHitbox();
		return this.hitbox.hitTest(hitbox.hitbox);
	}

	public void renderDebug(ShapeRenderer shapeRenderer) {
		updateHitbox();
		hitbox.renderDebug(shapeRenderer);
	}

	// sets the hitbox to position defined by vector2 position.
	private void updateHitbox() {
		if (entityData.getFlipX().get()) {
			hitbox.setX(entityData.getPosition().x + width - x - hitbox.getWidth());
		} else {
			hitbox.setX(entityData.getPosition().x + x);
		}
		hitbox.setY(entityData.getPosition().y + y);
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
