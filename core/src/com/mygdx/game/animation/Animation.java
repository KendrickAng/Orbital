package com.mygdx.game.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.shape.Rectangle;

import java.util.HashMap;
import java.util.TreeMap;

public class Animation {
	private Vector2 position;
	private boolean flipX;
	private boolean flipY;

	private float time;
	private float duration = 1f;
	private int frame;
	private int size;
	private boolean loop = true;

	private TreeMap<Integer, Sprite> frames;
	private HashMap<Integer, Rectangle> hitboxes;

	public Animation() {
		position = new Vector2();
		frames = new TreeMap<>();
		hitboxes = new HashMap<>();
	}

	public void put(int id, Pixmap pixmap) {
		frames.put(id, new Sprite(new Texture(pixmap)));

		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;
		for (int y = 0; y < pixmap.getHeight(); y++) {
			for (int x = 0; x < pixmap.getWidth(); x++) {
				if ((pixmap.getPixel(x, y) & 0x000000ff) == 0x000000ff) {
					minX = Math.min(x, minX);
					maxX = Math.max(x, maxX);
					minY = Math.min(y, minY);
					maxY = Math.max(y, maxY);
				}
			}
		}

		Rectangle hitbox = new Rectangle();
		hitbox.setX(minX);
		hitbox.setY(pixmap.getHeight() - maxY);
		hitbox.setWidth(maxX - minX);
		hitbox.setHeight(maxY - minY);
		hitboxes.put(id, hitbox);

		size = Math.max(id + 1, size);
		pixmap.dispose();
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public void setFlip(boolean flipX, boolean flipY) {
		this.flipX = flipX;
		this.flipY = flipY;
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

	public Rectangle getHitbox() {
		Rectangle hitbox = hitboxes.get(frame);
		Rectangle rectangle = new Rectangle();
		rectangle.setX(position.x + hitbox.getX());
		rectangle.setY(position.y + hitbox.getY());
		rectangle.setWidth(hitbox.getWidth());
		rectangle.setHeight(hitbox.getHeight());
		return rectangle;
	}
}
