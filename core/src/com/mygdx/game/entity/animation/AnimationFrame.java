package com.mygdx.game.entity.animation;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.entity.EntityData;
import com.mygdx.game.entity.Hitbox;

import java.util.HashMap;

public class AnimationFrame<P> {
	private EntityData entityData;
	private Sprite sprite;
	private HashMap<P, Hitbox> hitboxes;

	public AnimationFrame(FileHandle handle) {
		this.sprite = new Sprite(new Texture(handle));
		this.hitboxes = new HashMap<>();
	}

	public void addHitbox(P part, int minX, int maxX, int minY, int maxY, int width, int height) {
		hitboxes.put(part, new Hitbox(minX, maxX, minY, maxY, width, height));
	}

	public void setEntityData(EntityData entityData) {
		this.entityData = entityData;
		for (Hitbox hitbox: hitboxes.values()) {
			hitbox.setEntityData(entityData);
		}
	}

	public void render(SpriteBatch batch) {
		sprite.setPosition(entityData.getPosition().x, entityData.getPosition().y);
		sprite.setFlip(entityData.getFlipX().get(), false);
		sprite.setColor(entityData.getColor());
		sprite.draw(batch);
	}

	public void renderDebug(ShapeRenderer shapeRenderer) {
		for (Hitbox hitbox: hitboxes.values()) {
			hitbox.renderDebug(shapeRenderer);
		}
	}

	public Sprite getSprite() {
		return sprite;
	}

	public Hitbox getHitbox(P part) {
		return hitboxes.get(part);
	}
}
