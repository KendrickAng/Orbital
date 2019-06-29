package com.mygdx.game.entity.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.entity.EntityData;
import com.mygdx.game.entity.Hitbox;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * Contains all the animations for a certain state e.g Standing.
 *
 * @param <P> the enum grouping all the parts that need to be animated.
 */
public class Animation<P extends Enum> {
	// Total animation duration
	private float duration;
	private String directory;
	private HashMap<String, P> filenames;

	private int frame;
	private int frames;
	private boolean loaded;
	private boolean loop;

	private Timer timer;
	private HashMap<P, AnimationPart> parts; // Hashmap allows O(1) retrieval, and used for debug
	private TreeMap<P, AnimationPart> animations; // Treemap parts are rendered in order
	private HashMap<Integer, AnimationFrameTask> tasks;

	private AnimationEnd animationEnd;

	public Animation(float duration, String directory, HashMap<String, P> filenames) {
		this.duration = duration;
		this.directory = directory;
		this.filenames = filenames;

		this.timer = new Timer();
		this.parts = new HashMap<>();
		this.animations = new TreeMap<>();
		this.tasks = new HashMap<>();
	}

	// Load Animation assets
	public void load(EntityData entityData) {
		if (!loaded) {
			loaded = true;

			// map all parts to an empty animation, populates parts.
			for (P part : filenames.values()) {
				AnimationPart animation = new AnimationPart(entityData);
				parts.put(part, animation);
				animations.put(part, animation);
			}

			// returns filehandles for a directory.
			FileHandle[] files = Gdx.files.internal(directory).list();
			for (FileHandle file : files) {
				if (!file.isDirectory()) {
					// populates the animations in order, ensuring order dictated in assets name is followed.
					String[] n = file.nameWithoutExtension().split("_");
					int frame = Integer.parseInt(n[0]);
					String name = n[1];

					P part = filenames.get(name);
					if (part == null) {
						Gdx.app.error("Animation.java", "Part '" + name + "' is not defined.");
					} else {
						AnimationPart animation = parts.get(part);
						animation.put(frame, new Pixmap(file));
						frames = Math.max(frames, frame + 1);
					}
				}
			}
		}
	}

	public Animation<P> defineFrameTask(int frame, AnimationFrameTask task) {
		tasks.put(frame, task);
		return this;
	}

	public Animation<P> defineEnd(AnimationEnd animationEnd) {
		this.animationEnd = animationEnd;
		return this;
	}

	public void begin() {
		frame = 0;
		timer.clear();
		updateFrame();
	}

	private void updateFrame() {
		AnimationFrameTask task = tasks.get(frame);
		if (task != null) {
			task.call();
		}

		if (frame < frames - 1) {
			timer.scheduleTask(new Timer.Task() {
				@Override
				public void run() {
					Animation.this.frame++;
					Animation.this.updateFrame();
				}
			}, duration / frames);
		} else if (loop) {
			timer.scheduleTask(new Timer.Task() {
				@Override
				public void run() {
					Animation.this.frame = 0;
					Animation.this.updateFrame();
				}
			}, duration / frames);
		} else if (animationEnd != null) {
			timer.scheduleTask(new Timer.Task() {
				@Override
				public void run() {
					animationEnd.end();
				}
			}, duration / frames);
		}
	}

	public void render(SpriteBatch batch, Vector2 position, boolean flipX) {
		for (AnimationPart part : animations.values()) {
			Sprite sprite = part.getSprite(frame);

			// Ignore undefined sprites
			if (sprite != null) {
				sprite.setPosition(position.x, position.y);
				sprite.setFlip(flipX, false);
				sprite.draw(batch);
			}
		}
	}

	public void renderDebug(ShapeRenderer shapeRenderer) {
		for (AnimationPart part : animations.values()) {
			Hitbox hitbox = part.getHitbox(frame);

			// Ignore undefined hitboxes
			if (hitbox != null) {
				hitbox.renderDebug(shapeRenderer);
			}
		}
	}

	public Animation<P> loop() {
		this.loop = true;
		return this;
	}

	public void setPosition() {

	}

	public void setFlipX() {

	}

	/* Getters */
	public Hitbox getHitbox(P part) {
		return parts.get(part).getHitbox(frame);
	}
}
