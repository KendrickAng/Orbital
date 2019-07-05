package com.mygdx.game.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Hitbox {
	private EntityData entityData;

	private int width;
	private int height;
	private int offsetX;
	private int offsetY;
	private int textureWidth;
	private int textureHeight;

	public Hitbox(int minX, int maxX, int minY, int maxY, int textureWidth, int textureHeight) {
		this.offsetX = minX;
		this.offsetY = textureHeight - maxY;
		this.width = maxX - minX;
		this.height = maxY - minY;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
	}

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
			return entityData.getPosition().x + textureWidth - offsetX - width;
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
