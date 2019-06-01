package com.mygdx.game.entity.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.entity.Hitbox;

import java.util.HashMap;
import java.util.TreeMap;

public class Animation {
	private Vector2 position;
	private boolean flipX;
	private boolean flipY;

	private float time;
	private float duration;
	private int frame;
	private int size;
	private boolean loop = true;

	private TreeMap<Integer, Sprite> frames;
	private HashMap<Integer, Hitbox> hitboxes;

	public Animation() {
		this.duration = 1;
		frames = new TreeMap<>();
		hitboxes = new HashMap<>();
	}

	public void put(int frame, Pixmap pixmap) {
		frames.put(frame, new Sprite(new Texture(pixmap)));
		hitboxes.put(frame, new Hitbox(pixmap));

		size = Math.max(frame + 1, size);
		pixmap.dispose();
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public void setFlip(boolean flipX, boolean flipY) {
		this.flipX = flipX;
		this.flipY = flipY;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public void render(SpriteBatch batch) {
		if (time >= duration && loop) {
			time = 0;
		}

		if (time < duration) {
			frame = (int) (time / duration * size);

			Sprite sprite = frames.get(frame);
			sprite.setPosition(position.x, position.y);
			sprite.setFlip(flipX, flipY);
			sprite.draw(batch);
		}

		time += Gdx.graphics.getDeltaTime();
	}

	public Hitbox getHitbox() {
		Hitbox hitbox = hitboxes.get(frame);
		hitbox.setPosition(position);
		hitbox.setFlip(flipX, flipY);
		return hitbox;
	}
}
