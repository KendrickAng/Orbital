package com.untitled.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Hitbox represents a 2D rectangle with collision detection.
 */
public class Hitbox {
	private EntityData entityData;

	private int width;
	private int height;
	private int offsetX;
	private int offsetY;
	private int textureWidth;
	private int textureHeight;

	/**
	 * Creates a virtual 2D rectangle in a given texture dimension.
	 *
	 * @param minX          lowest x coordinate of the rectangle in the texture
	 * @param maxX          highest x coordinate of the rectangle in the texture
	 * @param minY          lowest y coordinate of the rectangle in the texture
	 * @param maxY          highest y coordinate of the rectangle in the texture
	 * @param textureWidth  width of the texture which the rectangle is in
	 * @param textureHeight height of the texture which the rectangle is in
	 */
	public Hitbox(int minX, int maxX, int minY, int maxY, int textureWidth, int textureHeight) {
		this.offsetX = minX;
		this.offsetY = textureHeight - maxY;
		this.width = maxX - minX;
		this.height = maxY - minY;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
	}

	/**
	 * Copy constructor
	 *
	 * @param hitbox the hitbox to copy.
	 */
	public Hitbox(Hitbox hitbox) {
		this.entityData = hitbox.entityData;
		this.width = hitbox.width;
		this.height = hitbox.height;
		this.offsetX = hitbox.offsetX;
		this.offsetY = hitbox.offsetY;
		this.textureWidth = hitbox.textureWidth;
		this.textureHeight = hitbox.textureHeight;
	}

	public void setEntityData(EntityData entityData) {
		this.entityData = entityData;
	}

	/**
	 * Collision detection of two hitboxes, using AABB collision detection.
	 *
	 * @param hitbox the other hitbox to test collision with.
	 * @return whether there is a collision with the other hitbox.
	 */
	public boolean hitTest(Hitbox hitbox) {
		return getX() < hitbox.getX() + hitbox.getWidth() &&
				getX() + getWidth() > hitbox.getX() &&
				getY() < hitbox.getY() + hitbox.getHeight() &&
				getY() + getHeight() > hitbox.getY();
	}

	/**
	 * Draws an outline of the hitbox.
	 *
	 * @param shapeRenderer {@link ShapeRenderer} to draw the outline on.
	 */
	public void renderDebug(ShapeRenderer shapeRenderer) {
		shapeRenderer.rect(getX(), getY(), width, height);
	}

	/**
	 * @return x coordinate of the hitbox in the world.
	 */
	public float getX() {
		if (entityData.getFlipX().get()) {
			return entityData.getPosition().x + textureWidth - offsetX - width;
		} else {
			return entityData.getPosition().x + offsetX;
		}
	}

	/**
	 * @return y coordinate of the hitbox in the world.
	 */
	public float getY() {
		return entityData.getPosition().y + offsetY;
	}

	/**
	 * @return x coordinate of the hitbox in the texture.
	 */
	public float getOffsetX() {
		return offsetX;
	}

	/**
	 * @return y coordinate of the hitbox in the texture.
	 */
	public int getOffsetY() {
		return offsetY;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public int getTextureWidth() {
		return textureWidth;
	}

	public int getTextureHeight() {
		return textureHeight;
	}
}
